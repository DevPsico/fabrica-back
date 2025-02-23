package com.estacionamento.estacionamento.controller;

import static org.mockito.BDDMockito.given; // Para o given
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.estacionamento.estacionamento.exceptions.ParkingSpotNotFoundException;
import com.estacionamento.estacionamento.models.ParkingSpot;
import com.estacionamento.estacionamento.models.VacancyStatus;
import com.estacionamento.estacionamento.models.VacancyType;
import com.estacionamento.estacionamento.services.ParkingSpotService;

@WebMvcTest(ParkingSpotController.class)
public class ParkingSpotControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ParkingSpotService parkingSpotService;

	// Teste para o endpoint POST /parking-spots
	@Test
	void createParkingSpot_ShouldReturnCreatedParkingSpot() throws Exception {
		// Configuração do mock
		ParkingSpot parkingSpot = new ParkingSpot();
		parkingSpot.setId(1L);
		parkingSpot.setNumero("C01");
		parkingSpot.setTipo(VacancyType.COMUM);
		parkingSpot.setStatus(VacancyStatus.DISPONIVEL);

		when(parkingSpotService.criarVaga(VacancyType.COMUM, VacancyStatus.DISPONIVEL)).thenReturn(parkingSpot);

		// Execução e verificação
		mockMvc.perform(post("/parking-spots").contentType(MediaType.APPLICATION_JSON)
				.content("{\"tipo\":\"COMUM\",\"status\":\"DISPONIVEL\"}"))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.numero").value("C01"))
				.andExpect(jsonPath("$.tipo").value("COMUM"))
				.andExpect(jsonPath("$.status").value("DISPONIVEL"));
	}

	// Teste para o endpoint GET /parking-spots
	@Test
	void getAllParkingSpots_ShouldReturnListOfParkingSpots() throws Exception {
		// Configuração do mock
		ParkingSpot parkingSpot1 = new ParkingSpot();
		parkingSpot1.setId(1L);
		parkingSpot1.setNumero("C01");
		parkingSpot1.setTipo(VacancyType.COMUM);
		parkingSpot1.setStatus(VacancyStatus.DISPONIVEL);

		ParkingSpot parkingSpot2 = new ParkingSpot();
		parkingSpot2.setId(2L);
		parkingSpot2.setNumero("C02");
		parkingSpot2.setTipo(VacancyType.COMUM);
		parkingSpot2.setStatus(VacancyStatus.RESERVADA);

		List<ParkingSpot> parkingSpots = Arrays.asList(parkingSpot1, parkingSpot2);

		when(parkingSpotService.findAll()).thenReturn(parkingSpots);

		// Execução e verificação
		mockMvc.perform(get("/parking-spots")).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(1))
				.andExpect(jsonPath("$[0].numero").value("C01")).andExpect(jsonPath("$[0].tipo").value("COMUM"))
				.andExpect(jsonPath("$[0].status").value("DISPONIVEL")).andExpect(jsonPath("$[1].id").value(2))
				.andExpect(jsonPath("$[1].numero").value("C02")).andExpect(jsonPath("$[1].tipo").value("COMUM"))
				.andExpect(jsonPath("$[1].status").value("OCUPADA"));
	}

	// Teste para o endpoint GET /parking-spots/{id}
	@Test
	void getParkingSpotById_ShouldReturnParkingSpot() throws Exception {
		// Configuração do mock
		ParkingSpot parkingSpot = new ParkingSpot();
		parkingSpot.setId(1L);
		parkingSpot.setNumero("C01");
		parkingSpot.setTipo(VacancyType.COMUM);
		parkingSpot.setStatus(VacancyStatus.DISPONIVEL);

		given(parkingSpotService.findById(1L)).willReturn(parkingSpot);

		// Execução e verificação
		mockMvc.perform(get("/parking-spots/{id}", 1L)).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1L))
				.andExpect(jsonPath("$.numero").value("C01")).andExpect(jsonPath("$.tipo").value("COMUM"))
				.andExpect(jsonPath("$.status").value("DISPONIVEL"));
	}

	// Teste para o endpoint PUT /parking-spots/{id}
	@Test
	void updateParkingSpot_ShouldReturnUpdatedParkingSpot() throws Exception {
		// Configuração do mock
		ParkingSpot existingParkingSpot = new ParkingSpot();
		existingParkingSpot.setId(1L);
		existingParkingSpot.setNumero("C01");
		existingParkingSpot.setTipo(VacancyType.COMUM);
		existingParkingSpot.setStatus(VacancyStatus.DISPONIVEL);

		ParkingSpot updatedParkingSpot = new ParkingSpot();
		updatedParkingSpot.setId(1L);
		updatedParkingSpot.setNumero("C01");
		updatedParkingSpot.setTipo(VacancyType.VIP);
		updatedParkingSpot.setStatus(VacancyStatus.RESERVADA);

		when(parkingSpotService.atualizarVaga(1L, updatedParkingSpot)).thenReturn(updatedParkingSpot);

		// Execução e verificação
		mockMvc.perform(put("/parking-spots/{id}", 1L).contentType(MediaType.APPLICATION_JSON)
				.content("{\"tipo\":\"VIP\",\"status\":\"RESERVADA\"}")).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1)).andExpect(jsonPath("$.numero").value("C01"))
				.andExpect(jsonPath("$.tipo").value("VIP")).andExpect(jsonPath("$.status").value("RESERVADA"));
	}

	// Teste para o endpoint DELETE /parking-spots/{id}
	@Test
	void deleteParkingSpot_WhenParkingSpotNotFound_ShouldReturnNotFound() throws Exception {
		// Configuração do mock
		doThrow(new ParkingSpotNotFoundException("Vaga não encontrada com o ID: 1")).when(parkingSpotService)
				.deleteById(1L);

		// Execução e verificação
		mockMvc.perform(delete("/parking-spots/{id}", 1L)).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value("Vaga não encontrada com o ID: 1"));
	}

}
