package com.estacionamento.estacionamento.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import lombok.Data;

@Data
public class ReservationDTO {

    private Long id;

    @NotNull(message = "A vaga de estacionamento não pode ser nula.")
    private ParkingSpotDTO parkingSpot;  

    @NotNull(message = "O cliente não pode ser nulo.")
    private CustomerDTO cliente;         

    @NotNull(message = "A data de início não pode ser nula.")
    private LocalDateTime dataInicio;

    private LocalDateTime dataFim;

    @Positive(message = "O valor total deve ser positivo.")
    private BigDecimal valorTotal;

    public ReservationDTO() {}

    public ReservationDTO(Long id, ParkingSpotDTO parkingSpotDTO, CustomerDTO clienteDTO, LocalDateTime dataInicio, LocalDateTime dataFim, BigDecimal valorTotal) {
        this.id = id;
        this.parkingSpot = parkingSpotDTO;
        this.cliente = clienteDTO;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.valorTotal = valorTotal;
    }

    public ReservationDTO(com.estacionamento.estacionamento.models.Reservation reservation) {
        this.id = reservation.getId();
        this.parkingSpot = new ParkingSpotDTO(reservation.getParkingSpot());
        this.cliente = new CustomerDTO(reservation.getCliente());
        this.dataInicio = reservation.getDataInicio();
        this.dataFim = reservation.getDataFim();
        this.valorTotal = reservation.getValorTotal();
    }
}
