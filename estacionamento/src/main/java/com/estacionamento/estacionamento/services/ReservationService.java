package com.estacionamento.estacionamento.services;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.estacionamento.estacionamento.models.Customer;
import com.estacionamento.estacionamento.models.ParkingSpot;
import com.estacionamento.estacionamento.models.Reservation;
import com.estacionamento.estacionamento.models.VacancyStatus;
import com.estacionamento.estacionamento.repository.CustomerRepository;
import com.estacionamento.estacionamento.repository.ParkingSpotRepository;
import com.estacionamento.estacionamento.repository.ReservationRepository;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ParkingSpotRepository parkingSpotRepository;

    @Autowired
    private CustomerRepository customerRepository;

    // Criar nova reserva
    public Reservation create(Long parkingSpotId, Long customerId, LocalDateTime dataInicio) {
    	
    	// Se dataInicio não for fornecida, usa o horário atual do computador
        if (dataInicio == null) {
        	dataInicio = ZonedDateTime.now(ZoneId.systemDefault()).toLocalDateTime();
        }
        ParkingSpot parkingSpot = parkingSpotRepository.findById(parkingSpotId)
                .orElseThrow(() -> new RuntimeException("Vaga não encontrada!"));

        if (!parkingSpot.isDisponivel()) {
            throw new IllegalStateException("A vaga não está disponível para reserva.");
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado!"));

        Reservation reservation = new Reservation();
        reservation.setParkingSpot(parkingSpot);
        reservation.setCliente(customer);
        reservation.setDataInicio(dataInicio);

        // Reserva a vaga
        parkingSpot.setStatus(VacancyStatus.RESERVADA);
        
        // Salvando a vaga com o novo status
        parkingSpotRepository.save(parkingSpot);

        return reservationRepository.save(reservation);
    }

    // Finalizar reserva
    public Reservation finalizarReserva(Long id, LocalDateTime dataFim) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada!"));

        if (reservation.getDataFim() != null) {
            throw new IllegalStateException("A reserva já foi finalizada.");
        }

        reservation.setDataFim(dataFim);
        reservation.setValorTotal(calcularValorTotal(reservation));

        // Libera a vaga
        reservation.getParkingSpot().setStatus(VacancyStatus.DISPONIVEL);

        return reservationRepository.save(reservation);
    }

    private BigDecimal calcularValorTotal(Reservation reservation) {
        // Calcula a diferença em horas, incluindo frações de hora
        long durationInMinutes = Duration.between(reservation.getDataInicio(), reservation.getDataFim()).toMinutes();
        BigDecimal valorPorHora = reservation.getParkingSpot().getTipo().getValorPorHora();

        // Converte a duração para horas, considerando frações
        BigDecimal durationInHours = BigDecimal.valueOf(durationInMinutes).divide(BigDecimal.valueOf(60), 2, BigDecimal.ROUND_UP);

        // Multiplica o valor por hora pela duração em horas
        return valorPorHora.multiply(durationInHours);
    }

    // Buscar todas as reservas
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    // Buscar reserva por ID
    public Optional<Reservation> findById(Long id) {
        return reservationRepository.findById(id);  // Agora retorna Optional
    }
}