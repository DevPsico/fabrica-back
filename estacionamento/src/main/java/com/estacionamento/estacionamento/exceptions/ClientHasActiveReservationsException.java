package com.estacionamento.estacionamento.exceptions;

public class ClientHasActiveReservationsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ClientHasActiveReservationsException(String message) {
		super(message);
	}


}
