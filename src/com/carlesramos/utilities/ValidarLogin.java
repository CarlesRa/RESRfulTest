package com.carlesramos.utilities;

/**
 * 
 * @author Juan Carlos Ramos
 * Clase encargada de almacenar los datos de la validación
 * del login.
 *
 */
public class ValidarLogin {
	
	private String idSession;
	private int idJugador;
	
	public ValidarLogin(String idSession, int idJugador) {
		this.idSession = idSession;
		this.idJugador = idJugador;
	}
	
	public String getIdSession() {
		return idSession;
	}
	
	public int getIdJugador() {
		return idJugador;
	}
	

	
	
}
