package com.estacionamento.estacionamento.services;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.estacionamento.estacionamento.exceptions.CustomerNotFoundException;
import com.estacionamento.estacionamento.exceptions.InvalidDataFimException;
import com.estacionamento.estacionamento.exceptions.NoReservationsFoundException;
import com.estacionamento.estacionamento.exceptions.ParkingSpotNotAvailableException;
import com.estacionamento.estacionamento.exceptions.ParkingSpotNotFoundException;
import com.estacionamento.estacionamento.exceptions.ReservationAlreadyFinalizedException;
import com.estacionamento.estacionamento.exceptions.ReservationNotFoundException;
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

    public Reservation create(Long parkingSpotId, Long customerId, LocalDateTime dataInicio) throws ParkingSpotNotAvailableException {
        // Validação dos parâmetros de entrada
        if (parkingSpotId == null) {
            throw new IllegalArgumentException("O ID da vaga não pode ser nulo.");
        }
        if (customerId == null) {
            throw new IllegalArgumentException("O ID do cliente não pode ser nulo.");
        }

        // Se dataInicio não for fornecida, usa o horário atual do computador
        if (dataInicio == null) {
            dataInicio = ZonedDateTime.now(ZoneId.systemDefault()).toLocalDateTime();
        }

        // Busca a vaga de estacionamento
        ParkingSpot parkingSpot = parkingSpotRepository.findById(parkingSpotId)
                .orElseThrow(() -> new ParkingSpotNotFoundException("Vaga não encontrada com o ID: " + parkingSpotId));

        // Verifica se a vaga está disponível
        if (!parkingSpot.isDisponivel()) {
            throw new ParkingSpotNotAvailableException("A vaga não está disponível para reserva.");
        }

        // Busca o cliente
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Cliente não encontrado com o ID: " + customerId));

        // Cria a reserva
        Reservation reservation = new Reservation();
        reservation.setParkingSpot(parkingSpot);
        reservation.setCliente(customer);
        reservation.setDataInicio(dataInicio);

        // Reserva a vaga (atualiza o status)
        parkingSpot.setStatus(VacancyStatus.RESERVADA);

        // Salvando a vaga com o novo status e a reserva
        parkingSpotRepository.save(parkingSpot);
        return reservationRepository.save(reservation);
    }

    public Reservation finalizarReserva(Long id, LocalDateTime dataFim) {
        // Busca a reserva pelo ID
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("Reserva não encontrada com o ID: " + id));

        // Verifica se a reserva já foi finalizada
        if (reservation.getDataFim() != null) {
            throw new ReservationAlreadyFinalizedException("A reserva já foi finalizada.");
        }

        // Validação de dataFim
        if (dataFim == null) {
            throw new IllegalArgumentException("O campo 'dataFim' é obrigatório.");
        }
        if (dataFim.isBefore(reservation.getDataInicio())) {
            throw new IllegalArgumentException("A data de fim deve ser posterior à data de início.");
        }

        // Atualiza a reserva
        reservation.setDataFim(dataFim);
        reservation.setValorTotal(calcularValorTotal(reservation));

        // Libera a vaga
        reservation.getParkingSpot().setStatus(VacancyStatus.DISPONIVEL);

        // Salva a reserva atualizada
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
        List<Reservation> reservations = reservationRepository.findAll();
        if (reservations.isEmpty()) {
            throw new NoReservationsFoundException("Nenhuma reserva encontrada.");
        }
        return reservations;
    }
    // Buscar reserva por ID
    public Reservation findById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("Reserva não encontrada com o ID: " + id));
    }
}