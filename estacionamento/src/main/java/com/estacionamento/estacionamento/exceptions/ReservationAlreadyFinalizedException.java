package com.estacionamento.estacionamento.exceptions;

public class ReservationAlreadyFinalizedException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ReservationAlreadyFinalizedException(String message) {
        super(message);
    }
}
