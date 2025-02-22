package com.estacionamento.estacionamento.exceptions;

public class NoCustomersFoundException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoCustomersFoundException(String message) {
        super(message);
    }
}
