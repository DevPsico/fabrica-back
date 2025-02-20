package com.estacionamento.estacionamento.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Reservation {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parking_spot_id", nullable = false)
    private ParkingSpot parkingSpot;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Customer cliente;

    @Column(nullable = false)
    private LocalDateTime dataInicio;

    @Column
    private LocalDateTime dataFim;

    @Column
    private BigDecimal valorTotal;

    // Construtores
    public Reservation() {
    }
/*
    public Reservation(ParkingSpot parkingSpot, Customer cliente, LocalDateTime dataInicio) {
        if (!parkingSpot.isDisponivel()) {
            throw new IllegalStateException("A vaga não está disponível para reserva.");
        }
        this.parkingSpot = parkingSpot;
        this.cliente = cliente;
        this.dataInicio = dataInicio;
        this.parkingSpot.reservar(); // Reserva a vaga
    }

    // Método para finalizar a reserva
    public void finalizarReserva(LocalDateTime dataFim) {
        this.dataFim = dataFim;
        this.valorTotal = calcularValorTotal();
        this.parkingSpot.liberar(); // Libera a vaga
    }

    // Método para calcular o valor total da reserva
    private BigDecimal calcularValorTotal() {
        if (dataFim == null) {
            throw new IllegalStateException("A reserva ainda não foi finalizada.");
        }
        long horas = java.time.Duration.between(dataInicio, dataFim).toHours();
        return parkingSpot.getValorPorHora().multiply(BigDecimal.valueOf(horas));
    }
    */

}
