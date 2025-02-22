package com.estacionamento.estacionamento.exceptions;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class StandardErro {
	
	@JsonFormat(pattern = "HH:mm - dd/MM/yyyy")
	private LocalDateTime timestem;
	private Integer status;
	private String message;
	private String path;

	public StandardErro(LocalDateTime timestem, Integer status, String message, String path) {
		super();
		this.timestem = timestem;
		this.status = status;
		this.message = message;
		this.path = path;
	}

}
