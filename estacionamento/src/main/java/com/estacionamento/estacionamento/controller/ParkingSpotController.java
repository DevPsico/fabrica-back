package com.estacionamento.estacionamento.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.estacionamento.estacionamento.dtos.ParkingSpotDTO;
import com.estacionamento.estacionamento.exceptions.ParkingSpotNotAvailableException;
import com.estacionamento.estacionamento.models.ParkingSpot;
import com.estacionamento.estacionamento.models.VacancyStatus;
import com.estacionamento.estacionamento.models.VacancyType;
import com.estacionamento.estacionamento.services.ParkingSpotService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/parking-spots")
public class ParkingSpotController {

	 @Autowired
	    private ParkingSpotService parkingSpotService;

	// Endpoint para criar uma nova vaga de estacionamento
	    @PostMapping
	    public ResponseEntity<ParkingSpotDTO> create(@Valid @RequestBody ParkingSpotDTO parkingSpotDTO) {
	        // Extrai os dados do DTO
	        VacancyType tipo = parkingSpotDTO.getTipo();
	        VacancyStatus status = parkingSpotDTO.getStatus();

	        // Cria a vaga no banco de dados usando o método criarVaga do serviço
	        ParkingSpot savedParkingSpot = parkingSpotService.criarVaga(tipo, status);

	        // Converte a entidade salva de volta para DTO
	        ParkingSpotDTO responseDTO = new ParkingSpotDTO(savedParkingSpot);

	        // Retorna o DTO com status 201 (CREATED)
	        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
	    }

	    // Endpoint para buscar todas as vagas de estacionamento
	    @GetMapping
	    public ResponseEntity<List<ParkingSpotDTO>> getAll() {
	        // Busca todas as vagas no banco de dados
	        List<ParkingSpot> parkingSpots = parkingSpotService.findAll();

	        // Converte a lista de entidades para uma lista de DTOs
	        List<ParkingSpotDTO> parkingSpotDTOs = parkingSpots.stream()
	                .map(ParkingSpotDTO::new)
	                .collect(Collectors.toList());

	        // Retorna a lista de DTOs com status 200 (OK)
	        return ResponseEntity.ok(parkingSpotDTOs);
	    }

	    // Endpoint para buscar uma vaga por ID
	    @GetMapping("/{id}")
	    public ResponseEntity<ParkingSpotDTO> getById(@PathVariable Long id) {
	        // Busca a vaga pelo ID
	        ParkingSpot parkingSpot = parkingSpotService.findById(id);

	        // Converte a entidade para DTO
	        ParkingSpotDTO parkingSpotDTO = new ParkingSpotDTO(parkingSpot);

	        // Retorna o DTO com status 200 (OK)
	        return ResponseEntity.ok(parkingSpotDTO);
	    }

	    @PutMapping("/{id}")
	    public ResponseEntity<ParkingSpotDTO> update(@PathVariable Long id, @Valid @RequestBody ParkingSpotDTO parkingSpotDTO) {
	        // Verifique se a vaga de estacionamento existe
	        ParkingSpot existingParkingSpot = parkingSpotService.findById(id);
	        if (existingParkingSpot == null) {
	            // Se não existir, retorna um erro 404
	            return ResponseEntity.notFound().build();
	        }

	        // Converte o DTO para a entidade ParkingSpot
	        ParkingSpot parkingSpot = new ParkingSpot(parkingSpotDTO);

	        // Atualiza a vaga no banco de dados
	        ParkingSpot updatedParkingSpot = parkingSpotService.atualizarVaga(id, parkingSpot);

	        // Converte a entidade atualizada de volta para DTO
	        ParkingSpotDTO responseDTO = new ParkingSpotDTO(updatedParkingSpot);

	        // Retorna o DTO com status 200 (OK)
	        return ResponseEntity.ok(responseDTO);
	    }

	    // Endpoint para deletar uma vaga de estacionamento por ID
	    @DeleteMapping("/{id}")
	    public ResponseEntity<Void> deleteById(@PathVariable Long id) throws ParkingSpotNotAvailableException {
	        // Deleta a vaga pelo ID
	        parkingSpotService.deleteById(id);

	        // Retorna status 204 (NO_CONTENT) para indicar sucesso na exclusão
	        return ResponseEntity.noContent().build();
	    }
}