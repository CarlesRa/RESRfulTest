package com.carlesramos.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import com.carlesramos.hibernateutility.HibernateUtil;
import com.carlesramos.utilities.GameManager;
import com.carlesramos.utilities.JugadaCpu;
import com.carlesramos.utilities.NuevaPartida;
import com.google.gson.Gson;
import model.Cartas;
import model.Jugadores;
import model.Partidas;
import java.util.UUID;

@Path("/inicio")
public class PartidaApi extends ResourceConfig{
	
	private GameManager manager;
	private SessionFactory sFactory;
	private Session session;
	private Transaction transaction;
	
	//GET METHODS
	
	@GET
	@Path("/saluda")
	public Response saluda() {
		Gson g = new Gson();
		return Response.status(Response.Status.OK)
				.entity(g.toJson("Bienvenido!!!"))
				.build();
	}
	
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
	
	
	
	@GET
	@Path("/resultadoPartida")
	@Produces(MediaType.APPLICATION_JSON)
	public int resultadoPartida(@QueryParam("idPartida")int idPartida,
			@QueryParam("idJugadorA")int idJugadorA,
			@QueryParam("numVictoriasJugadorA")int numVictoriasJugadorA,
			@QueryParam("idJugadorB")int idJugadorB,
			@QueryParam("numVictoriasJugadorB")int numVictoriasJugadorB) {
		manager = GameManager.getInstance();
		return manager.resultadoPartida(idPartida, idJugadorA,
				numVictoriasJugadorA, idJugadorB, numVictoriasJugadorB);
		
	}
	
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
	
	@POST
	@Path("/nuevaPartida")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response nuevaPartida(@FormParam("idSession")String idSession,
			@FormParam("idCliente")int idCliente) {
		Gson g = new Gson();
		manager = GameManager.getInstance();
		String message = manager.nuevaPartida(idSession, idCliente);
		return Response.status(Response.Status.OK)
				.entity(message)
				.build();
				
	}
	
	@POST
	@Path("/comprobarJugada")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response comprobarJugada(@FormParam("idPartida")String idPartida,
			@FormParam("cartaJugadorA")int idCartaA,
			@FormParam("caracteristica")String caracteristica,
			@FormParam("cartaJugadorB")int idCartaB){
		manager = GameManager.getInstance();
		String message = String.valueOf(manager.comprobarJugada(idPartida, idCartaA, caracteristica, idCartaB));
		return Response.status(Response.Status.OK)
				.entity(message)
				.build();
				
	}
	
	@POST
	@Path("/cartas")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void insertarCarta(String jsonCarta) {
		manager = GameManager.getInstance();
		manager.insertarCarta(jsonCarta);
	}
}
