package com.estacionamento.estacionamento.models;

import com.estacionamento.estacionamento.dtos.ParkingSpotDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class ParkingSpot {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String numero;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private VacancyType tipo;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private VacancyStatus status  = VacancyStatus.DISPONIVEL;

	// Construtores
	public ParkingSpot() {
	}
	
	public ParkingSpot(Long id) {
	    this.id = id;
	}

	public ParkingSpot(String numero, VacancyType tipo, VacancyStatus status) {
		this.numero = numero;
		this.tipo = tipo;
		this.status = status;
	}
	
	// Método para verificar se a vaga está disponível
    public boolean isDisponivel() {
        return this.status == VacancyStatus.DISPONIVEL;
    }
    
    public ParkingSpot(ParkingSpotDTO parkingSpotDTO) {
        this.id = parkingSpotDTO.getId();
        this.numero = parkingSpotDTO.getNumero();
        this.tipo = parkingSpotDTO.getTipo();
        this.status = parkingSpotDTO.getStatus();
    }
}