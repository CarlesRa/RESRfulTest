package com.carlesramos.utilities;

import model.Cartas;

public class JugadaCpu {
	private Cartas carta;
	private String caracteristica;
	
	public JugadaCpu(Cartas carta, String caracteristica) {
		super();
		this.carta = carta;
		this.caracteristica = caracteristica;
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
