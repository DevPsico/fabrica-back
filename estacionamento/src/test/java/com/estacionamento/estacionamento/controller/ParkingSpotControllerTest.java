package com.estacionamento.estacionamento.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

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

import com.estacionamento.estacionamento.dtos.ParkingSpotDTO;
import com.estacionamento.estacionamento.exceptions.ParkingSpotNotAvailableException;
import com.estacionamento.estacionamento.models.ParkingSpot;
import com.estacionamento.estacionamento.models.VacancyStatus;
import com.estacionamento.estacionamento.models.VacancyType;
import com.estacionamento.estacionamento.services.ParkingSpotService;

@ExtendWith(MockitoExtension.class)
class ParkingSpotControllerTest {

	@Mock
	private ParkingSpotService parkingSpotService;

	@InjectMocks
	private ParkingSpotController parkingSpotController;

	private ParkingSpot parkingSpot;
	private ParkingSpotDTO parkingSpotDTO;

	@BeforeEach
	void setUp() {
		parkingSpot = new ParkingSpot("C01", VacancyType.COMUM, VacancyStatus.DISPONIVEL);
		parkingSpot.setId(1L); // Definindo o ID manualmente para o teste
		parkingSpotDTO = new ParkingSpotDTO(parkingSpot);
	}

	@Test
	void create_ShouldReturnCreatedParkingSpotDTO() {
		when(parkingSpotService.criarVaga(any(VacancyType.class), any(VacancyStatus.class))).thenReturn(parkingSpot);

		ResponseEntity<ParkingSpotDTO> response = parkingSpotController.create(parkingSpotDTO);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("C01", response.getBody().getNumero());
		assertEquals(VacancyType.COMUM, response.getBody().getTipo());
		assertEquals(VacancyStatus.DISPONIVEL, response.getBody().getStatus());
	}

	@Test
	void getAll_ShouldReturnListOfParkingSpots() {
		when(parkingSpotService.findAll()).thenReturn(Arrays.asList(parkingSpot));

		ResponseEntity<List<ParkingSpotDTO>> response = parkingSpotController.getAll();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertFalse(response.getBody().isEmpty());
		assertEquals(1, response.getBody().size());
		assertEquals("C01", response.getBody().get(0).getNumero());
	}

	@Test
	void getById_ShouldReturnParkingSpotDTO_WhenParkingSpotExists() {
		when(parkingSpotService.findById(1L)).thenReturn(parkingSpot);

		ResponseEntity<ParkingSpotDTO> response = parkingSpotController.getById(1L);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("C01", response.getBody().getNumero());
	}

	@Test
	void update_ShouldReturnUpdatedParkingSpotDTO() {
		when(parkingSpotService.findById(1L)).thenReturn(parkingSpot);
		when(parkingSpotService.atualizarVaga(any(Long.class), any(ParkingSpot.class))).thenReturn(parkingSpot);

		ResponseEntity<ParkingSpotDTO> response = parkingSpotController.update(1L, parkingSpotDTO);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("C01", response.getBody().getNumero());
	}

	@Test
	void deleteById_ShouldReturnNoContent() throws ParkingSpotNotAvailableException {
		doNothing().when(parkingSpotService).deleteById(1L);

		ResponseEntity<Void> response = parkingSpotController.deleteById(1L);

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}
}
