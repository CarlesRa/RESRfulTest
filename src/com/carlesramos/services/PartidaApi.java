package com.carlesramos.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.server.ResourceConfig;
import com.carlesramos.utilities.GameManager;
import com.google.gson.Gson;
/**
 * 
 * @author Juan Carlos Ramos
 * Clase con los métodos del ApiRest
 *
 */
@Path("/inicio")
public class PartidaApi extends ResourceConfig{
	
	private GameManager manager;
	
	//GET METHODS
	
	@GET
	@Path("/saluda")
	public Response saluda() {
		Gson g = new Gson();
		return Response.status(Response.Status.OK)
				.entity(g.toJson("Bienvenido!!!"))
				.build();
	}
	/**
	 *Comprueba si existe el nickName en la base de datos.
	 * @param nickName
	 * @return si existe o no.
	 */
	@GET
	@Path("/nickName")
	@Produces(MediaType.APPLICATION_JSON)
	public Response nickNameExist(@QueryParam("nickName")String nickName){
		Gson g = new Gson();
		manager = GameManager.getInstance();
		return Response.status(Response.Status.OK)
				.entity(g.toJson(manager.nickNameExists(nickName)))
				.build();
	}
	
	/**
	 * Inicia una jugada de la cpu
	 * @return la carta seleccionada y la característica a jugar.
	 */
	@GET
	@Path("/juegaCPU")
	@Produces(MediaType.APPLICATION_JSON)
	public Response juegaCPU() {
		manager = GameManager.getInstance();
		String jugadaCpu = manager.jugarCpu();
		return Response.status(Response.Status.OK)
				.entity(jugadaCpu)
				.build();
	}
	
	
	
	
	/**
	 * Muestra una lista de todos los jugadores
	 * @return Lista de todos los jugadores ordenada por puntuación
	 */
	@GET
	@Path("/jugadores")
	@Produces(MediaType.APPLICATION_JSON)
	public Response jugadores() {
		
		manager = GameManager.getInstance();
		String message = manager.getClasificacion();
		
		return Response.status(Response.Status.OK)
				.entity(message)
				.build();
	}
	
	/**
	 * Muestra todas las cartas
	 * @return Lista de todas las cartas ordenadas por puntuación
	 */
	@GET
	@Path("/cartas")
	@Produces(MediaType.APPLICATION_JSON)
	public Response cartas() {
		manager = GameManager.getInstance();
		String message = manager.getClasificacionCartas();
		
		return Response.status(Response.Status.OK)
				.entity(message)
				.build();
	}
	
	//POST METHODS
	/**
	 * Valida si el usuario se encuentra en la base de datos
	 * @param nickName
	 * @param password
	 * @return true o false
	 */
	@POST
	@Path("/validarLogin")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response validarLogin(@FormParam("nickName")String nickName,
			@FormParam("pass")String password){
		System.out.println(nickName + " " + password);
		manager = GameManager.getInstance();
		String message = manager.validarLogin(nickName, password);
		return Response.status(Response.Status.OK)
				.entity(message)
				.build();
	}
	
	/**
	 *Inserta un nuevo jugador en la base de datos
	 * @param jugador json de un jugador
	 * @return si se ha insertado correctamente o no
	 */
	@POST
	@Path("/jugadores")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response insertJugador(@FormParam("jugador")String jugador){
		
		System.out.println(jugador.toString());
		manager = GameManager.getInstance();
		String message = manager.insertarJugador(jugador);
		return Response.status(Response.Status.OK)
				.entity(message)
				.build();
	}
	
	/**
	 * inicia una nueva partida
	 * @param idSession
	 * @param idCliente
	 * @return retorna un objeto NuevaPartida
	 */
	@POST
	@Path("/nuevaPartida")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response nuevaPartida(@FormParam("idSession")String idSession,
			@FormParam("idCliente")int idCliente) {
		Gson g = new Gson();
		System.out.println(idCliente + " " + idSession);
		manager = GameManager.getInstance();
		String message = manager.nuevaPartida(idSession, idCliente);
		return Response.status(Response.Status.OK)
				.entity(message)
				.build();
				
	}
	
	/**
	 * Comprueba una jugada
	 * @param idPartida
	 * @param idCartaA
	 * @param caracteristica
	 * @param idCartaB
	 * @return El resultado de la jugada.
	 */
	@POST
	@Path("/comprobarJugada")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response comprobarJugada(@FormParam("idPartida")String idPartida,
			@FormParam("cartaJugadorA")int idCartaA,
			@FormParam("caracteristica")String caracteristica,
			@FormParam("cartaJugadorB")int idCartaB){
		Gson g = new Gson();
		manager = GameManager.getInstance();
		String message = String.valueOf(manager.comprobarJugada(idPartida, idCartaA, caracteristica, idCartaB));
		return Response.status(Response.Status.OK)
				.entity(g.toJson(message))
				.build();
				
	}
	
	/**
	 * Comprueba el resultado de la partida
	 * @param idPartida
	 * @param idJugadorB
	 * @return El id del ganador.
	 */
	@POST
	@Path("/resultadoPartida")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response resultadoPartida(@FormParam("idPartida")int idPartida,
			@FormParam("idCliente")int idJugadorB) {
		Gson g = new Gson();
		System.out.println(idJugadorB + " " + idPartida);
		manager = GameManager.getInstance();
		int message = manager.resultadoPartida(idPartida, 1,idJugadorB);
		return Response.status(Response.Status.OK)
				.entity(g.toJson(message))
				.build();
				
		
	}
	
	/**
	 * Inserta una carta en la base de datos.
	 * @param jsonCarta
	 */
	@POST
	@Path("/cartas")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void insertarCarta(String jsonCarta) {
		manager = GameManager.getInstance();
		manager.insertarCarta(jsonCarta);
	}
}
