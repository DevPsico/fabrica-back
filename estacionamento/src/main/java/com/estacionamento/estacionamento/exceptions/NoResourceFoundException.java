package com.estacionamento.estacionamento.exceptions;

public class NoResourceFoundException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoResourceFoundException(String message) {
        super(message);
    }
}
