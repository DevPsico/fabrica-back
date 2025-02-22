package com.estacionamento.estacionamento.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.estacionamento.estacionamento.exceptions.ParkingSpotNotAvailableException;
import com.estacionamento.estacionamento.exceptions.ReservationNotFoundException;
import com.estacionamento.estacionamento.models.Customer;
import com.estacionamento.estacionamento.models.ParkingSpot;
import com.estacionamento.estacionamento.models.Reservation;
import com.estacionamento.estacionamento.models.VacancyStatus;
import com.estacionamento.estacionamento.models.VacancyType;
import com.estacionamento.estacionamento.repository.CustomerRepository;
import com.estacionamento.estacionamento.repository.ParkingSpotRepository;
import com.estacionamento.estacionamento.repository.ReservationRepository;
import com.estacionamento.estacionamento.services.ReservationService;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ParkingSpotRepository parkingSpotRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private ReservationService reservationService;

    private ParkingSpot parkingSpot;
    private Customer customer;
    private Reservation reservation;

    @BeforeEach
    void setUp() {
        // Configuração comum para os testes
        parkingSpot = new ParkingSpot();
        parkingSpot.setId(1L);
        parkingSpot.setStatus(VacancyStatus.DISPONIVEL);

        customer = new Customer();
        customer.setId(1L);

        reservation = new Reservation();
        reservation.setId(1L);
        reservation.setParkingSpot(parkingSpot);
        reservation.setCliente(customer);
        reservation.setDataInicio(LocalDateTime.now());
    }

    // Teste para criar uma reserva
    @Test
    void createReservation_ShouldReturnReservation() throws ParkingSpotNotAvailableException {
        // Configuração dos mocks
        when(parkingSpotRepository.findById(1L)).thenReturn(Optional.of(parkingSpot));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        // Execução do método
        Reservation createdReservation = reservationService.create(1L, 1L, null);

        // Verificações
        assertNotNull(createdReservation);
        assertEquals(VacancyStatus.RESERVADA, parkingSpot.getStatus());
        verify(parkingSpotRepository, times(1)).save(parkingSpot);
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    // Teste para criar uma reserva com vaga indisponível
    @Test
    void createReservation_WhenParkingSpotNotAvailable_ShouldThrowException() {
        // Configuração do mock
        parkingSpot.setStatus(VacancyStatus.RESERVADA);
        when(parkingSpotRepository.findById(1L)).thenReturn(Optional.of(parkingSpot));

        // Execução e verificação da exceção
        assertThrows(ParkingSpotNotAvailableException.class, () -> {
            reservationService.create(1L, 1L, null);
        });
    }

    // Teste para finalizar uma reserva
    @Test
    void finalizarReserva_ShouldUpdateReservationAndFreeParkingSpot() {
        // Configuração do mock
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        // Configuração do parkingSpot na reserva
        ParkingSpot parkingSpot = new ParkingSpot();
        parkingSpot.setId(1L);
        parkingSpot.setStatus(VacancyStatus.RESERVADA);
        parkingSpot.setTipo(VacancyType.COMUM); // Usando o enum VacancyType.COMUM

        reservation.setParkingSpot(parkingSpot);

        // Configuração da data de início e fim
        LocalDateTime dataInicio = LocalDateTime.of(2025, 2, 21, 8, 0, 0); // Reserva iniciada às 08:00
        LocalDateTime dataFim = LocalDateTime.of(2025, 2, 21, 10, 0, 0); // Reserva finalizada às 10:00
        reservation.setDataInicio(dataInicio);

        // Execução do método
        Reservation finalizedReservation = reservationService.finalizarReserva(1L, dataFim);

        // Verificações
        assertNotNull(finalizedReservation.getDataFim());
        assertNotNull(finalizedReservation.getValorTotal());
        assertEquals(VacancyStatus.DISPONIVEL, parkingSpot.getStatus());
        verify(reservationRepository, times(1)).save(reservation);
    }

    // Teste para buscar todas as reservas
    @Test
    void findAll_ShouldReturnListOfReservations() {
        // Configuração do mock
        when(reservationRepository.findAll()).thenReturn(Collections.singletonList(reservation));

        // Execução do método
        List<Reservation> reservations = reservationService.findAll();

        // Verificações
        assertFalse(reservations.isEmpty());
        assertEquals(1, reservations.size());
    }

    // Teste para buscar uma reserva por ID
    @Test
    void findById_ShouldReturnReservation() {
        // Configuração do mock
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        // Execução do método
        Reservation foundReservation = reservationService.findById(1L);

        // Verificações
        assertNotNull(foundReservation);
        assertEquals(1L, foundReservation.getId());
    }

    // Teste para buscar uma reserva por ID inexistente
    @Test
    void findById_WhenReservationNotFound_ShouldThrowException() {
        // Configuração do mock
        when(reservationRepository.findById(1L)).thenReturn(Optional.empty());

        // Execução e verificação da exceção
        assertThrows(ReservationNotFoundException.class, () -> {
            reservationService.findById(1L);
        });
    }
}
