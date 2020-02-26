package com.carlesramos.utilities;

import model.Cartas;

/**
 * 
 * @author Juan Carlos Ramos
 * Clase encargada de almacenar los datos de jugada de la CPU
 *
 */
public class JugadaCpu {
	private Cartas carta;
	private String caracteristica;
	private int ganadasCPU;
	private int ganadasPlayer;
	
	public JugadaCpu(Cartas carta, String caracteristica, int ganadasCPU, int ganadasPlayer) {
		super();
		this.carta = carta;
		this.caracteristica = caracteristica;
		this.ganadasCPU = ganadasCPU;
		this.ganadasPlayer = ganadasPlayer;
	}

	public Cartas getCarta() {
		return carta;
	}

	public String getCaracterística() {
		return caracteristica;
	}

	public void setCarta(Cartas carta) {
		this.carta = carta;
	}

	public void setCaracterística(String caracteristica) {
		this.caracteristica = caracteristica;
	}
	
	
}
