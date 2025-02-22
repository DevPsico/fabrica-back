package com.estacionamento.estacionamento.exceptions;

public class NoReservationsFoundException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoReservationsFoundException(String message) {
        super(message);
    }
}
