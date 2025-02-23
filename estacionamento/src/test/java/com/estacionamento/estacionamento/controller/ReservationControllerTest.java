package com.estacionamento.estacionamento.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
import com.estacionamento.estacionamento.exceptions.FieldMessage;
import com.estacionamento.estacionamento.exceptions.ParkingSpotNotAvailableException;
import com.estacionamento.estacionamento.exceptions.ReservationAlreadyFinalizedException;
import com.estacionamento.estacionamento.exceptions.ReservationNotFoundException;
import com.estacionamento.estacionamento.exceptions.StandardErro;
import com.estacionamento.estacionamento.exceptions.ValidationError;
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
	void getAll_ShouldReturnEmptyList_WhenNoReservationsExist() {
		when(reservationService.findAll()).thenReturn(Arrays.asList());

		List<ReservationDTO> result = reservationController.getAll();

		assertTrue(result.isEmpty());
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
	void getById_ShouldReturnNotFound_WhenReservationDoesNotExist() {
		when(reservationService.findById(1L)).thenThrow(new ReservationNotFoundException("Reserva não encontrada"));

		assertThrows(ReservationNotFoundException.class, () -> {
			reservationController.getById(1L);
		});
	}

	@Test
	void create_ShouldReturnCreatedReservationDTO() throws ParkingSpotNotAvailableException {
		when(reservationService.create(any(Long.class), any(Long.class), any(LocalDateTime.class)))
				.thenReturn(reservation);

		ResponseEntity<ReservationDTO> response = reservationController.create(reservationDTO);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(1L, response.getBody().getId());
	}

	@Test
	void create_ShouldReturnBadRequest_WhenDataIsInvalid() throws ParkingSpotNotAvailableException {
		reservationDTO.setParkingSpot(null); // Dados inválidos

		assertThrows(IllegalArgumentException.class, () -> {
			reservationController.create(reservationDTO);
		});
	}

	@Test
	void create_ShouldReturnBadRequest_WhenParkingSpotIdIsNull() {
		reservationDTO.getParkingSpot().setId(null);

		assertThrows(IllegalArgumentException.class, () -> {
			reservationController.create(reservationDTO);
		});
	}

	@Test
	void create_ShouldReturnBadRequest_WhenCustomerIdIsNull() {
		reservationDTO.getCliente().setId(null);

		assertThrows(IllegalArgumentException.class, () -> {
			reservationController.create(reservationDTO);
		});
	}

	@Test
	void create_ShouldReturnBadRequest_WhenParkingSpotIsNotAvailable() throws ParkingSpotNotAvailableException {
		when(reservationService.create(any(Long.class), any(Long.class), any(LocalDateTime.class)))
				.thenThrow(new ParkingSpotNotAvailableException("A vaga não está disponível para reserva."));

		assertThrows(ParkingSpotNotAvailableException.class, () -> {
			reservationController.create(reservationDTO);
		});
	}

	@Test
	void create_ShouldAcceptFutureDataInicio() throws ParkingSpotNotAvailableException {
		LocalDateTime futureDate = LocalDateTime.now().plusDays(1);
		reservationDTO.setDataInicio(futureDate);

		when(reservationService.create(any(Long.class), any(Long.class), eq(futureDate))).thenReturn(reservation);

		ResponseEntity<ReservationDTO> response = reservationController.create(reservationDTO);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(1L, response.getBody().getId());
	}

	@Test
	void finalizarReserva_ShouldReturnUpdatedReservationDTO() {
		// Preenche o campo dataFim no DTO
		reservationDTO.setDataFim(LocalDateTime.now());

		// Simula o comportamento do serviço
		when(reservationService.finalizarReserva(any(Long.class), any(LocalDateTime.class))).thenReturn(reservation);

		// Chama o método do controller
		ResponseEntity<?> response = reservationController.finalizarReserva(1L, reservationDTO);

		// Verifica o status da resposta
		assertEquals(HttpStatus.OK, response.getStatusCode());

		// Verifica o corpo da resposta
		assertNotNull(response.getBody());
		assertTrue(response.getBody() instanceof ReservationDTO); // Verifica se o corpo é do tipo ReservationDTO

		ReservationDTO responseDTO = (ReservationDTO) response.getBody(); // Faz o cast para ReservationDTO
		assertEquals(1L, responseDTO.getId()); // Verifica o ID da reserva
	}

	@Test
	void finalizarReserva_ShouldReturnBadRequest_WhenReservationIsAlreadyFinalized() {
		// Configura o dataFim na reserva para simular uma reserva já finalizada
		reservation.setDataFim(LocalDateTime.now());

		// Configura o dataFim no DTO
		reservationDTO.setDataFim(LocalDateTime.now().plusHours(1));

		// Simula o comportamento do serviço para lançar a exceção
		when(reservationService.finalizarReserva(any(Long.class), any(LocalDateTime.class)))
				.thenThrow(new ReservationAlreadyFinalizedException("A reserva já foi finalizada."));

		// Chama o método do controller e verifica a resposta
		ResponseEntity<?> response = reservationController.finalizarReserva(1L, reservationDTO);

		// Verifica o status da resposta
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		// Verifica o corpo da resposta
		StandardErro errorResponse = (StandardErro) response.getBody();
		assertNotNull(errorResponse);
		assertEquals("A reserva já foi finalizada.", errorResponse.getMessage());
		assertEquals("/reservations/1", errorResponse.getPath());
	}

	@Test
	void finalizarReserva_ShouldReturnBadRequest_WhenDataFimIsNull() {
		// Configura o dataFim no DTO como nulo
		reservationDTO.setDataFim(null);

		// Chama o método do controller e verifica a resposta
		ResponseEntity<?> response = reservationController.finalizarReserva(1L, reservationDTO);

		// Verifica o status da resposta
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		// Verifica o corpo da resposta
		ValidationError validationError = (ValidationError) response.getBody();
		assertNotNull(validationError);
		assertEquals("Erro de validação", validationError.getMessage());
		assertEquals("/reservations/1", validationError.getPath());

		// Verifica os erros de campo
		List<FieldMessage> errors = validationError.getErros();
		assertFalse(errors.isEmpty());
		assertEquals("dataFim", errors.get(0).getFieldName());
		assertEquals("O campo 'dataFim' é obrigatório.", errors.get(0).getMessage());
	}

}
