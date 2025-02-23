package com.estacionamento.estacionamento.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.estacionamento.estacionamento.dtos.ReservationDTO;
import com.estacionamento.estacionamento.exceptions.ParkingSpotNotAvailableException;
import com.estacionamento.estacionamento.models.Customer;
import com.estacionamento.estacionamento.models.ParkingSpot;
import com.estacionamento.estacionamento.models.Reservation;
import com.estacionamento.estacionamento.models.VacancyStatus;
import com.estacionamento.estacionamento.models.VacancyType;
import com.estacionamento.estacionamento.services.ReservationService;

@ExtendWith(MockitoExtension.class)
public class ReservationControllerTest {
	
	@Mock
    private ReservationService reservationService;

    @InjectMocks
    private ReservationController reservationController;

    private Reservation reservation;
    private ReservationDTO reservationDTO;

    @BeforeEach
    void setUp() {
        // Criando uma vaga de estacionamento
        ParkingSpot parkingSpot = new ParkingSpot("C01", VacancyType.COMUM, VacancyStatus.DISPONIVEL);
        parkingSpot.setId(1L);

        // Criando um cliente
        Customer customer = new Customer(1L, "João Silva");

        // Criando uma reserva
        reservation = new Reservation();
        reservation.setId(1L);
        reservation.setParkingSpot(parkingSpot);
        reservation.setCliente(customer);
        reservation.setDataInicio(LocalDateTime.now());
        reservation.setValorTotal(BigDecimal.valueOf(20.0));

        // Criando o DTO correspondente
        reservationDTO = new ReservationDTO(reservation);
    }

    @Test
    void getAll_ShouldReturnListOfReservations() {
        when(reservationService.findAll()).thenReturn(Arrays.asList(reservation));

        List<ReservationDTO> result = reservationController.getAll();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void getById_ShouldReturnReservationDTO_WhenReservationExists() {
        when(reservationService.findById(1L)).thenReturn(reservation);

        ResponseEntity<ReservationDTO> response = reservationController.getById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void create_ShouldReturnCreatedReservationDTO() throws ParkingSpotNotAvailableException {
        when(reservationService.create(any(Long.class), any(Long.class), any(LocalDateTime.class))).thenReturn(reservation);

        ResponseEntity<ReservationDTO> response = reservationController.create(reservationDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void finalizarReserva_ShouldReturnUpdatedReservationDTO() {
        // Configura o dataFim no DTO
        reservationDTO.setDataFim(LocalDateTime.now().plusHours(2)); // Adiciona 2 horas ao dataInicio

        // Simula o comportamento do serviço
        when(reservationService.finalizarReserva(any(Long.class), any(LocalDateTime.class))).thenReturn(reservation);

        // Chama o método do controller
        ResponseEntity<ReservationDTO> response = reservationController.finalizarReserva(1L, reservationDTO);

        // Verifica o status e o corpo da resposta
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
    }

}
