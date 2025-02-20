package com.estacionamento.estacionamento.dtos;

import java.math.BigDecimal;
import com.estacionamento.estacionamento.models.VacancyStatus;
import lombok.Data;


@Data
public class ParkingSpotDTO {
	
	private String numero;
    private String tipo;
    private BigDecimal valorPorHora;
    private VacancyStatus status;

}
