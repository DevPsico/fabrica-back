package com.estacionamento.estacionamento.dtos;

import com.estacionamento.estacionamento.models.ParkingSpot;
import com.estacionamento.estacionamento.models.VacancyStatus;
import com.estacionamento.estacionamento.models.VacancyType;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class ParkingSpotDTO {

    private Long id;
    
    private String numero;
    @NotNull(message = "O tipo da vaga é obrigatório.") // Valida se o tipo não é nulo
    private VacancyType tipo;
    @NotNull(message = "O status da vaga é obrigatório.") // Valida se o status não é nulo
    private VacancyStatus status = VacancyStatus.DISPONIVEL;

    // Construtores
    public ParkingSpotDTO() {
    }
    
	public ParkingSpotDTO(Long id, String numero, @NotNull(message = "O tipo da vaga é obrigatório.") VacancyType tipo,
			@NotNull(message = "O status da vaga é obrigatório.") VacancyStatus status) {
		super();
		this.id = id;
		this.numero = numero;
		this.tipo = tipo;
		this.status = status;
	}
    
    

    public ParkingSpotDTO(ParkingSpot parkingSpot) {
        this.id = parkingSpot.getId();
        this.numero = parkingSpot.getNumero();
        this.tipo = parkingSpot.getTipo();
        this.status = parkingSpot.getStatus();
    }





}