package com.carlesramos.services;

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
}
