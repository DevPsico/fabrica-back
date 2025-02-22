package com.estacionamento.estacionamento.exceptions;

public class ParkingSpotNotFoundException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ParkingSpotNotFoundException(String message) {
        super(message);
    }
}
