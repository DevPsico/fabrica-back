package com.estacionamento.estacionamento.exceptions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardErro{

	private List<FieldMessage> erros = new ArrayList<>();

	public ValidationError(LocalDateTime timestem, Integer status, String message, String path) {
		super(timestem, status, message, path);
		// TODO Auto-generated constructor stub
	}

	public List<FieldMessage> getErros() {
		return erros;
	}

	public void addErros(String fieldName, String message) {
		this.erros.add(new FieldMessage(fieldName, message));
	}

}
