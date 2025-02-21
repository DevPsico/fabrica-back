package com.estacionamento.estacionamento.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ReservationDTO {

    private Long id;
    private ParkingSpotDTO parkingSpot;  
    private CustomerDTO cliente;          // Também pode ser substituído por CustomerDTO
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private BigDecimal valorTotal;
	
	public ReservationDTO() {

	}
	
	public ReservationDTO(Long id, ParkingSpotDTO parkingSpotDTO, CustomerDTO clienteDTO, LocalDateTime dataInicio, LocalDateTime dataFim, BigDecimal valorTotal) {
        this.id = id;
        this.parkingSpot = parkingSpotDTO;
        this.cliente = clienteDTO;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.valorTotal = valorTotal;
    }

	// Construtor para converter o modelo Reservation em DTO
    public ReservationDTO(com.estacionamento.estacionamento.models.Reservation reservation) {
    	 this.id = reservation.getId();
    	    // Convertendo ParkingSpot para ParkingSpotDTO
    	    this.parkingSpot = new ParkingSpotDTO(reservation.getParkingSpot());
    	    // Convertendo Customer para CustomerDTO
    	    this.cliente = new CustomerDTO(reservation.getCliente());
    	    this.dataInicio = reservation.getDataInicio();
    	    this.dataFim = reservation.getDataFim();
    	    this.valorTotal = reservation.getValorTotal();
    }
}