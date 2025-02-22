package com.estacionamento.estacionamento.exceptions;

public class ParkingSpotNotAvailableException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ParkingSpotNotAvailableException(String message) {
        super(message);
    }

}
