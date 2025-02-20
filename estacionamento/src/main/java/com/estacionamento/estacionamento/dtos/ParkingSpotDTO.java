package com.estacionamento.estacionamento.dtos;

import com.estacionamento.estacionamento.models.ParkingSpot;
import com.estacionamento.estacionamento.models.VacancyStatus;
import com.estacionamento.estacionamento.models.VacancyType;
import lombok.Data;


@Data
public class ParkingSpotDTO {

    private Long id;
    private String numero;
    private VacancyType tipo;
    private VacancyStatus status = VacancyStatus.DISPONIVEL;

    // Construtores
    public ParkingSpotDTO() {
    }

    public ParkingSpotDTO(ParkingSpot parkingSpot) {
        this.id = parkingSpot.getId();
        this.numero = parkingSpot.getNumero();
        this.tipo = parkingSpot.getTipo();
        this.status = parkingSpot.getStatus();
    }

}