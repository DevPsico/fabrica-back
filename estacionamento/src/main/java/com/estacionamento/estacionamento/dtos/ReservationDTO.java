package com.estacionamento.estacionamento.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ReservationDTO {

	private Long parkingSpotId;
	private LocalDateTime dataInicio;
	private LocalDateTime dataFim;
	private BigDecimal valorTotal;
}
