package com.carlesramos.utilities;

import java.util.ArrayList;

import model.Cartas;

/**
 * 
 * @author Juan Carlos Ramos
 * Clase encargada de almacenar los datos de una nueva partida
 *
 */
public class NuevaPartida {
	private ArrayList<Cartas> cartasCliente;
	private int idPartida;
	private int jugadorInicial;
	
	public NuevaPartida(ArrayList<Cartas>cartas, int idPartida, int jugadorInicial) {
		this.cartasCliente = cartas;
		this.idPartida = idPartida;
		this.jugadorInicial = jugadorInicial;
	}
	
	public NuevaPartida(int idPartida, int jugadorInicial) {
		this.idPartida = idPartida;
		this.jugadorInicial = jugadorInicial;
	}

	public int getIdPartida() {
		return idPartida;
	}

	public int getJugadorInicial() {
		return jugadorInicial;
	}	
	
	public ArrayList<Cartas> getCartasCliente(){
		return cartasCliente;
	}
}
