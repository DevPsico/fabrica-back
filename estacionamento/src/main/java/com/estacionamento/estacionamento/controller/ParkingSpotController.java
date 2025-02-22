package com.estacionamento.estacionamento.controller;

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

import com.estacionamento.estacionamento.models.ParkingSpot;
import com.estacionamento.estacionamento.services.ParkingSpotService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/parking-spots")
public class ParkingSpotController {

	@Autowired
	private ParkingSpotService parkingSpotService;

	// Endpoint para criar uma nova vaga de estacionamento
	@PostMapping
	public ResponseEntity<ParkingSpot> create(@Valid @RequestBody ParkingSpot parkingSpot) {
		ParkingSpot savedParkingSpot = parkingSpotService.criarVaga(parkingSpot.getTipo(), parkingSpot.getStatus());
		return ResponseEntity.status(HttpStatus.CREATED).body(savedParkingSpot); // Retorna a vaga criada com status 201
	}

	// Endpoint para buscar todas as vagas de estacionamento
	@GetMapping
	public ResponseEntity<Iterable<ParkingSpot>> getAll() {
		Iterable<ParkingSpot> parkingSpots = parkingSpotService.findAll();
		return ResponseEntity.ok(parkingSpots); // Retorna todas as vagas com status 200
	}

	// Endpoint para buscar uma vaga por ID
    @GetMapping("/{id}")
    public ResponseEntity<ParkingSpot> getById(@PathVariable Long id) {
        ParkingSpot parkingSpot = parkingSpotService.findById(id);
        return ResponseEntity.ok(parkingSpot);
    }
	

	// Endpoint para atualizar uma vaga de estacionamento existente
	@PutMapping("/{id}")
	public ResponseEntity<ParkingSpot> update(@PathVariable Long id, @RequestBody ParkingSpot parkingSpotDetails) {
		ParkingSpot updatedParkingSpot = parkingSpotService.atualizarVaga(id, parkingSpotDetails);
		return ResponseEntity.ok(updatedParkingSpot); // Retorna a vaga atualizada com status 200
	}

	// Endpoint para deletar uma vaga de estacionamento por ID
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable Long id) {
	    parkingSpotService.deleteById(id);
	    return ResponseEntity.noContent().build(); // Retorna 204 (No Content)
	}

}