package com.carlesramos.services;

import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.carlesramos.utilities.GameManager;

import model.Jugadores;

@Path("/crud")
public class CrudApi {

	private GameManager manager;
	
	//JUGADORES
	
	@GET
	@Path("/jugadores")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getJugadores() {
		manager = GameManager.getInstance();
		String respuesta = manager.getClasificacion();
		return Response.status(Response.Status.OK)
				.entity(respuesta).build();
	}
	
	@POST
	@Path("/jugadores")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response insertarJugador(@FormParam("nickName")String nickName,
			@FormParam("email")String email, @FormParam("password")String passwd,
			@FormParam("ganadas")int ganadas, @FormParam("empatadas")int empatadas,
			@FormParam("perdidas")int perdidas){
		
		manager = GameManager.getInstance();
		String message = manager.insertarJugador(nickName, email, passwd, ganadas, empatadas, perdidas);
		return Response.status(Response.Status.OK)
				.entity(message)
				.build();
	}
	
	@PUT
	@Path("/jugadores")
	public Response modificarJugador(@QueryParam("idJugador")int idJugador, 
			@QueryParam("nickName")String nickName,
			@QueryParam("email")String email, @FormParam("password")String passwd,
			@QueryParam("ganadas")int ganadas, @FormParam("empatadas")int empatadas,
			@QueryParam("perdidas")int perdidas) {
		manager = GameManager.getInstance();
		String messages = manager.modificarJugador(idJugador, nickName, email, passwd, ganadas, empatadas, perdidas);
		return Response.status(Response.Status.OK)
				.entity(messages)
				.build();
		//
	}
	
	@DELETE
	@Path("/jugadores")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response borrarJugador(@QueryParam("idJugador")int idJugador) {
		manager = GameManager.getInstance();
		String message = manager.borrarJugador(idJugador);
		return Response.status(Response.Status.OK)
				.entity(message)
				.build();
	}
	
	@GET
	@Path("/cartas")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCartas() {
		manager = GameManager.getInstance();
		String message = manager.getClasificacionCartas();
		return Response.status(Response.Status.OK)
				.entity(message)
				.build();
	}
	
	@POST
	@Path("/cartas")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response insertarCarta(@FormParam("marca")String marca, @FormParam("modelo")String modelo,
			@FormParam("pais")String pais, @FormParam("motor")float motor, 
			@FormParam("potencia")int potencia, @FormParam("cilindros")int cilindros, @FormParam("velocidad")int velocidad,
			@FormParam("revoluciones")int revoluciones, @FormParam("consumo")float consumo, @FormParam("rondasGanadas")int rondasG) {
		manager = GameManager.getInstance();
		String response = manager.insertarCarta(marca, modelo, pais, motor, potencia,
				cilindros, velocidad, revoluciones, consumo, rondasG);
		return Response.status(Response.Status.OK)
				.entity(response)
				.build();
	}
	
	@PUT
	@Path("/cartas")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response modificarCarta(@QueryParam("idCarta")int idCarta,@QueryParam("marca")String marca, @QueryParam("modelo")String modelo,
			@QueryParam("pais")String pais, @QueryParam("motor")float motor, 
			@QueryParam("potencia")int potencia, @QueryParam("cilindros")int cilindros, @QueryParam("velocidad")int velocidad,
			@QueryParam("revoluciones")int revoluciones, @QueryParam("consumo")float consumo, @QueryParam("rondasGanadas")int rondasG) {
		manager = GameManager.getInstance();
		String response = manager.insertarCarta(marca, modelo, pais, motor, potencia,
				cilindros, velocidad, revoluciones, consumo, rondasG);
		return Response.status(Response.Status.OK)
				.entity(response)
				.build();
	}
	
	@DELETE
	@Path("/cartas")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response borrarCarta(@QueryParam("idCarta")int idCarta) {
		manager = GameManager.getInstance();
		String response = manager.borrarCarta(idCarta);
		return Response.status(Response.Status.OK)
				.entity(response)
				.build();	
	}
	
	@GET
	@Path("/partidas")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPartidas() {
		manager = GameManager.getInstance();
		String response = manager.getPartidas();
		return Response.status(Response.Status.OK)
				.entity(response)
				.build();	
	}
	
	@POST
	@Path("/partidas")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response insertarPartida(@FormParam("idSession")String idSession, @FormParam("jugadorA")int jugadorA,
			@FormParam("jugadorB")int jugadorB, @FormParam("ganador")int ganador, 
			@FormParam("terminada")boolean terminada, @FormParam("fecha")Date fecha) {
		
		String response = manager.insertarPartida(idSession, jugadorA, jugadorB, ganador, terminada, fecha);
		return Response.status(Response.Status.OK)
				.entity(response)
				.build();	
	}
	
	@PUT
	@Path("/partidas")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response modificarPartida(@QueryParam("idPartida")int idPartida,@QueryParam("idSession")String idSession, 
			@QueryParam("jugadorA")int jugadorA,
			@QueryParam("jugadorB")int jugadorB, @QueryParam("ganador")int ganador, 
			@QueryParam("terminada")boolean terminada, @QueryParam("fecha")Date fecha) {
		manager = GameManager.getInstance();
		String response = manager.modificarPartida(idPartida, idSession, jugadorA, jugadorB, ganador, terminada, fecha);
		return Response.status(Response.Status.OK)
				.entity(response)
				.build();	
		
	}
	@DELETE
	@Path("/partidas")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response borrarPartida(@QueryParam("idPartida")int idPartida) {
		manager = GameManager.getInstance();
		String response = manager.borrarPartida(idPartida);
		return Response.status(Response.Status.OK)
				.entity(response)
				.build();	
	}
}
