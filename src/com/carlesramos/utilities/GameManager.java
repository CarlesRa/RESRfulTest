package com.carlesramos.utilities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.ws.rs.FormParam;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import com.carlesramos.hibernateutility.HibernateUtil;
import com.google.gson.Gson;
import model.Cartas;
import model.Jugadores;
import model.Partidas;

public class GameManager {
	
	enum Caracteristicas {
		Motor, Potencia, Velocidad, 
		Cilindros, Revoluciones, Consumo
	}
	//Constans del joc
	private static final int EMPATE = 0;
	private static final int JUGADOR_1 = 1;
	private static final int JUGADOR_2 = 2;
	private static final String LOGIN_FAIL = "Login_fail";
	private static final int ERROR = -1;
	private static final int CARTAS_JUGAR = 12;
	private static final int ID_CPU = 1;
	private static GameManager instance;
	
	//Variables hibernate
	private SessionFactory sFactory;
	private Session session;
	private Transaction transaction;
	
	//Variables joc
	private ArrayList<Cartas>cartasCPU;
	private Cartas cartaA;
	private Cartas cartaB;
	private int idJugadorInicial;
	private int rondasGanadasJugador1;
	private int rondasGanadasJugador2;
	private Jugadores jugador1;
	private Jugadores jugador2;
	private Partidas partida;
	private Random rnd;
		
	private GameManager() {
		cartasCPU = new ArrayList<>();
		rnd = new Random();
	}
	
	public static GameManager getInstance() {
		if (instance == null) {
			instance = new GameManager();
		}
		return instance;
	}
	
	
	public void setIdJugadorInicial(int idJugador) {
		this.idJugadorInicial = idJugador;
	}
	
	public int getIdJugadorInicial() {
		return idJugadorInicial;
	}
	
	public int getRondasGanadasJugador1() {
		return rondasGanadasJugador1;
	}

	public int getRondasGanadasJugador2() {
		return rondasGanadasJugador2;
	}

	public void setRondasGanadasJugador1(int rondasGanadasJugador1) {
		this.rondasGanadasJugador1 = rondasGanadasJugador1;
	}

	public void setRondasGanadasJugador2(int rondasGanadasJugador2) {
		this.rondasGanadasJugador2 = rondasGanadasJugador2;
	}
	
	
	//METODES PER COMPROVAR EL TRANSCURS DEL JOC
	
	

	public boolean nickNameExists(String nickName) {
		Gson g = new Gson();
		sFactory = HibernateUtil.getSessionFactory();
		session = sFactory.openSession();
		Query<?> query = session.createQuery("from Jugadores where nickName = :nickName");
		query.setParameter("nickName", nickName);
		List<?> list = query.getResultList();
		if (list.size() == 0) {
			return false;
		}
		else return true;
	}
	
	public String validarLogin(String nickName, String password) {
		Gson g = new Gson();
		String sha1 = Lib.obtenerSHA1(nickName, password);
		sFactory = HibernateUtil.getSessionFactory();
		session = sFactory.openSession();
		 
		Query<?> query = session.createQuery("select idJugador from Jugadores where nickName = :nickName"
				+ " and password = :password");
		query.setParameter("nickName", nickName);
		query.setParameter("password", sha1);
		List<?> list = query.getResultList();
		if (list.size() > 0) {
			String idCliente =  String.valueOf(list.get(0));
			String uuidSession = UUID.randomUUID().toString();
			String validar = uuidSession + " " + idCliente;
			return g.toJson(validar);
		}
		else return g.toJson(LOGIN_FAIL);
	}
	
	public String nuevaPartida(String idSession, int idCliente) {
		
		Partidas partida;
		NuevaPartida nuevaPartida;
		Gson g = new Gson();
		int jugadorInicial = rnd.nextInt(2);
		sFactory = HibernateUtil.getSessionFactory();
		session = sFactory.openSession();
		transaction = session.beginTransaction();
		
		if (jugadorInicial == 0) {
			partida = new Partidas(idSession, ID_CPU, idCliente, 0, false, new Date());
			setIdJugadorInicial(ID_CPU);
		}
		else {
			partida = new Partidas(idSession, idCliente, ID_CPU, 0 , false, new Date());
			setIdJugadorInicial(idCliente);
		}
		session.save(partida);
		transaction.commit();
		
		//faig el repartiment de cartes
		ArrayList<Cartas> todasLasCartas;
		ArrayList<Cartas> cartasCliente = new ArrayList<>();
		ArrayList<Cartas> cartasJuego;
		sFactory = HibernateUtil.getSessionFactory();
		session = sFactory.openSession();
		Query<?> query= session.createQuery("from Cartas");
		todasLasCartas = (ArrayList<Cartas>)query.getResultList();
		cartasJuego = getCartasJuego(todasLasCartas);
		for (int i=0; i<cartasJuego.size(); i++) {
			if(i < 6) {
				cartasCPU.add(cartasJuego.get(i));
			}
			else if (i > 5) {
				cartasCliente.add(cartasJuego.get(i));
			}
		}
		//obtenc la partida que he creat
		query = session.createQuery("from Partidas order by idPartida DESC");
		List<?>partidas = query.getResultList();
		partida = (Partidas)partidas.get(0);
		session.close();
		//cree el objecte nova partida
		nuevaPartida = new NuevaPartida(cartasCliente, partida.getIdPartida(), 1);
		return g.toJson(nuevaPartida);
	}
	
	public String jugarCpu() {
		Gson g = new Gson();
		int posCarta = rnd.nextInt(cartasCPU.size());
		Cartas carta = cartasCPU.get(posCarta);
		cartasCPU.remove(posCarta);
		String caracteristica = Caracteristicas.values()
				[rnd.nextInt(Caracteristicas.values().length)].toString();
		JugadaCpu jugadaCpu = new JugadaCpu(carta, caracteristica);
		return g.toJson(jugadaCpu);
	}

	public String comprobarJugada(String idPartida, int idCartaA, String caracteristica,
			int idCartaB) {
		int resultado = -1;
		int idCartaCpu;
		String caracteristicaCpu;
		sFactory = HibernateUtil.getSessionFactory();
		session = sFactory.openSession();

		//Arreplegue la carta del jugador A
		Query<?> query= session.createQuery("from Cartas where idCarta = :idCarta");
		query.setParameter("idCarta", idCartaA);
		List<?>cartasA = query.getResultList();
		if (cartasA != null) {
			cartaA = (Cartas)cartasA.get(0);
		}
		else return "-1";

		//Arerplegue la carta del jugador B
		query = session.createQuery("from Cartas where idCarta = :idCarta");
		query.setParameter("idCarta", idCartaB);
		List<?>cartasB = query.getResultList();
		if (cartasB != null) {
			cartaB = (Cartas)cartasA.get(0);
		}
		else return "-1";

		//Comprove la jugada segons el atribut seleccionat
		switch (caracteristica) {
			case "Motor" : {
				if (cartaA.getMotor() < cartaB.getMotor()) {
					resultado = JUGADOR_2;
				}
				else if (cartaA.getMotor() == cartaB.getMotor()) {
					resultado = EMPATE;
				}
				else resultado = JUGADOR_1;
			break;
			}
			case "Potencia" : {
				if (cartaA.getPotenciaKv() < cartaB.getPotenciaKv()) {
					resultado = JUGADOR_2;
				}
				else if (cartaA.getPotenciaKv() == cartaB.getPotenciaKv()) {
					resultado = EMPATE;
				}
				else resultado = JUGADOR_1;
			break;
			}
			case "Velocidad" : {
				if (cartaA.getVelocidad() < cartaB.getVelocidad()) {
					resultado = JUGADOR_2;
				}
				else if (cartaA.getVelocidad() == cartaB.getVelocidad()) {
					resultado = EMPATE;
				}
				else resultado = JUGADOR_1;
			break;
			}
			case "Cilindros" : {
				if (cartaA.getNumCilindros() < cartaB.getNumCilindros()) {
					resultado = JUGADOR_2;
				}
				else if (cartaA.getNumCilindros() == cartaB.getNumCilindros()) {
					resultado = EMPATE;
				}
				else resultado = JUGADOR_1;
				break;
			}
			case "Revoluciones" : {
				if (cartaA.getRevoluciones() < cartaB.getRevoluciones()) {
					resultado = JUGADOR_1;
				}
				else if (cartaA.getRevoluciones() == cartaB.getRevoluciones()) {
					resultado = EMPATE;
				}
				else resultado = JUGADOR_2;
				break;
			}
			case "Consumo" : {
				if (cartaA.getConsumo() < cartaB.getConsumo()) {
					resultado = JUGADOR_1;
				}
				else if (cartaA.getConsumo() == cartaB.getConsumo()) {
					resultado = EMPATE;
				}
				else resultado = JUGADOR_2;
				break;
			}
		}

		//Faig el update de rondes guanyades de la carta
		transaction = session.beginTransaction();
		if (resultado == JUGADOR_1) {
			cartaA.setRondasGanadas(cartaA.getRondasGanadas() + 1);
			session.update(cartaA);
		}
		else if (resultado == JUGADOR_2) {
			cartaB.setRondasGanadas(cartaB.getRondasGanadas() + 1);
			session.update(cartaB);
		}
		transaction.commit();

		session.close();
		return String.valueOf(resultado);
	}
	
	
	public int resultadoPartida(int idSession,int idJugadorA, int numVictoriasJugadorA,
			int idJugadorB, int numVictoriasJugadorB) {
		Query<?> query;
		query = session.createQuery("from Jugadores where idJugador = :idJugador");
		query.setParameter("idJugador", idJugadorA);
		List<?>jugadorA = query.getResultList();
		if (jugadorA != null) {
			jugador1 = (Jugadores)jugadorA.get(0);
		}	

		query = session.createQuery("from Jugadores where idJugador = :idJugador");
		query.setParameter("idJugador", idJugadorB);
		List<?>jugadorB = query.getResultList();
		if (jugadorB != null) {
			jugador2 = (Jugadores)jugadorA.get(0);
		}	

		query = session.createQuery("from Partidas where idSession = :idSession");
		query.setParameter("idSession", idSession);
		List<?>p = query.getResultList();
		if (p != null) {
			partida = (Partidas)p.get(0);
		}

		if (numVictoriasJugadorA > numVictoriasJugadorB) {

			if (jugadorA != null) {
				jugador1.setGanadas(jugador1.getGanadas() + 1);
				partida.setGanador(jugador1.getIdJugador());
				session.update(jugador1);
				session.update(partida);
				transaction.commit();
				session.close();
				return JUGADOR_1;
			}
		}
		else if(numVictoriasJugadorA == numVictoriasJugadorB) {
			jugador1.setEmpatadas(jugador1.getEmpatadas() + 1);
			jugador2.setEmpatadas(jugador2.getEmpatadas() + 1);
			session.update(jugador1);
			session.update(jugador2);
			transaction.commit();
			session.close();
			return EMPATE;
		}
		else {
			jugador2.setGanadas(jugador2.getGanadas() + 1);
			partida.setGanador(jugador2.getIdJugador());
			session.update(jugador2);
			session.update(partida);
			transaction.commit();
			session.close();
			return JUGADOR_2;
		}
		return ERROR;
	}
	
	public String insertarJugador(String jugador) {
		Gson g = new Gson();
		Jugadores j = g.fromJson(jugador, Jugadores.class);
		sFactory = HibernateUtil.getSessionFactory();
		session = sFactory.openSession();
		transaction = session.beginTransaction();
		session.save(j);
		transaction.commit();
		session.close();
		return jugador;
	}
	
	public String insertarJugador(String nick, String email, String pass,
			int gan, int per, int emp) {
		Gson g = new Gson();
		Jugadores j = new Jugadores(nick, email, pass, gan, per, emp);
		sFactory = HibernateUtil.getSessionFactory();
		session = sFactory.openSession();
		transaction = session.beginTransaction();
		session.save(j);
		transaction.commit();
		session.close();
		return g.toJson(j);
	}
	
	public String modificarJugador(int id, String nick, String email, String pass,
			int gan, int per, int emp) {
		sFactory = HibernateUtil.getSessionFactory();
		session = sFactory.openSession();
		Jugadores jugador = new Jugadores();
		Query<?> query;
		query = session.createQuery("from Jugadores where idJugador = :idJugador");
		query.setParameter("idJugador", id);
		List<?>jugadorA = query.getResultList();
		if( jugadorA.size() > 0) {
			transaction = session.beginTransaction();
			jugador = (Jugadores)jugadorA.get(0);
			session.update(jugador);
		}	

		
		if(nick != null) {
			jugador.setNickName(nick);
		}
		if(email != null) {
			jugador.setEmail(email);
		}
		if(pass != null) {
			jugador.setPassword(pass);
		}
		if(gan > 0) {
			jugador.setGanadas(gan);
		}
		if(per > 0) {
			jugador.setPerdidas(per);
		}
		if(emp > 0) {
			jugador.setEmpatadas(emp);
		}
		transaction.commit();
		session.close();
		return"";
	}
	
	public String borrarJugador(int idJugador) {
		
		sFactory = HibernateUtil.getSessionFactory();
		session = sFactory.openSession();
		Jugadores jugador = new Jugadores();
		Query<?> query;
		query = session.createQuery("from Jugadores where idJugador = :idJugador");
		query.setParameter("idJugador", idJugador);
		List<?>jugadorA = query.getResultList();
		if (jugadorA != null) {
			transaction = session.beginTransaction();
			jugador = (Jugadores)jugadorA.get(0);
			session.delete(jugador);
			transaction.commit();
		}
		session.close();
		return " ";
	}
	
	public void insertarCarta(String jsonCarta) {
		sFactory = HibernateUtil.getSessionFactory();
		session = sFactory.openSession();
		transaction = session.beginTransaction();
		Gson gson = new Gson();
		Cartas carta = gson.fromJson(jsonCarta, Cartas.class); 
		session.save(carta);
		transaction.commit();
		session.close();
	}
	
	public ArrayList<Cartas> getCartasJuego(ArrayList<Cartas>cartas){
		
		ArrayList<Cartas>cartasAux = cartas;
		ArrayList<Cartas>cartasJuego = new ArrayList<>();
		for (int i=0; i<CARTAS_JUGAR; i++) {
			int posicion = rnd.nextInt(cartasAux.size());
			cartasJuego.add(cartasAux.get(posicion));
			cartasAux.remove(posicion);
		}
		
		return cartasJuego;
		
	}
	
	public Cartas getCartaCPU() {
		int posicion = rnd.nextInt(cartasCPU.size());
		Cartas carta = cartasCPU.get(posicion);
		cartasCPU.remove(posicion);
		return carta;
	}
	
	public String getClasificacion() {
		Gson g = new Gson();
		ArrayList<Jugadores> jugadores;
		sFactory = HibernateUtil.getSessionFactory();
		session = sFactory.openSession();
		Query q = session.createQuery("from Jugadores order by ganadas desc");
		jugadores = (ArrayList<Jugadores>)q.getResultList();
		session.close();
		return g.toJson(jugadores);
	}
	
	public String getClasificacionCartas() {
		Gson g = new Gson();
		ArrayList<Cartas> cartas;
		sFactory = HibernateUtil.getSessionFactory();
		session = sFactory.openSession();
		Query q = session.createQuery("from Cartas order by rondasGanadas desc");
		cartas = (ArrayList<Cartas>)q.getResultList();
		return g.toJson(cartas);
	}
	
	public String insertarCarta(String marca, String modelo, String pais, float motor, 
			int potencia, int cilindros, int velocidad, int revoluciones, float consumo, int rondasG) {
		Gson g = new Gson();
		sFactory = HibernateUtil.getSessionFactory();
		Cartas carta = new Cartas(marca, modelo,pais,motor,potencia,cilindros,velocidad,
				revoluciones, consumo, rondasG);
		session = sFactory.openSession();
		transaction = session.beginTransaction();
		session.save(carta);
		transaction.commit();
		session.close();
		return g.toJson(carta);
	}
	
	public String modificarCarta(int id, String marca, String modelo, String pais, float motor, 
			int potencia, int cilindros, int velocidad, int revoluciones, float consumo, int rondasG) {
		Gson g = new Gson();
		Cartas carta;
		sFactory = HibernateUtil.getSessionFactory();
		session = sFactory.openSession();
		transaction = session.beginTransaction();
		Query q = session.createQuery("from Cartas where idCarta= :idCarta");
		q.setParameter("idCarta", id);
		carta =(Cartas) q.getResultList();
		carta.setMarca(marca);
		carta.setModelo(modelo);
		carta.setPais(pais);
		carta.setMotor(motor);
		carta.setPotenciaKv(potencia);
		carta.setNumCilindros(cilindros);
		carta.setVelocidad(velocidad);
		carta.setRevoluciones(revoluciones);
		carta.setConsumo(consumo);
		carta.setRondasGanadas(rondasG);
		session.update(carta);
		transaction.commit();
		session.close();
		return g.toJson(carta);
	}
	
	public String borrarCarta(int idCarta) {
		Cartas carta;
		Gson g = new Gson();
		sFactory = HibernateUtil.getSessionFactory();
		session = sFactory.openSession();
		Query<?> query;
		query = session.createQuery("from Cartas where idCarta = :idCarta");
		query.setParameter("idCarta", idCarta);
		carta = (Cartas)query.getResultList();
		session.delete(carta);
		return g.toJson(carta);
	}
	
	public String getPartidas() {
		Gson g = new Gson();
		ArrayList<Partidas> partidas;
		sFactory = HibernateUtil.getSessionFactory();
		session = sFactory.openSession();
		transaction = session.beginTransaction();
		Query<?> query;
		query = session.createQuery("from Partidas");
		partidas = (ArrayList<Partidas>)query.getResultList();
		transaction.commit();
		session.close();
		return g.toJson(partidas);
	}
	
	public String insertarPartida(String idSession, int jugadorA,
			int jugadorB,int ganador, boolean terminada,Date fecha) {
		Gson g = new Gson();
		Partidas partida = new Partidas(idSession, jugadorA, jugadorB,
				ganador, terminada, fecha);
		sFactory = HibernateUtil.getSessionFactory();
		session = sFactory.openSession();
		transaction = session.beginTransaction();
		session.save(partida);
		transaction.commit();
		session.close();
		return g.toJson(partida);
	}
	
	public String modificarPartida(int idPartida, String idSession, int jugadorA,
			int jugadorB,int ganador, boolean terminada,Date fecha) {
		Gson g = new Gson();
		Partidas partida;
		sFactory = HibernateUtil.getSessionFactory();
		session = sFactory.openSession();
		transaction = session.beginTransaction();
		Query<?> query;
		query = session.createQuery("from Partidas where idPartida= :idPartida");
		query.setParameter("idPartida", idPartida);
		partida = (Partidas)query.getResultList();
		partida.setIdSession(idSession);
		partida.setJugadorA(jugadorA);
		partida.setJugadorB(jugadorB);
		partida.setTerminada(terminada);
		partida.setGanador(ganador);
		partida.setFecha(fecha);
		session.update(partida);
		transaction.commit();
		session.close();
		return g.toJson(partida);
	}
	public String borrarPartida(int idPartida) {
		Gson g = new Gson();
		Partidas partida;
		sFactory = HibernateUtil.getSessionFactory();
		session = sFactory.openSession();
		transaction = session.beginTransaction();
		Query<?> query;
		query = session.createQuery("from Partidas where idPartida= :idPartida");
		query.setParameter("idPartida", idPartida);
		partida = (Partidas)query.getResultList();
		session.delete(partida);
		transaction.commit();
		session.close();
		return g.toJson(partida);
	}
}
