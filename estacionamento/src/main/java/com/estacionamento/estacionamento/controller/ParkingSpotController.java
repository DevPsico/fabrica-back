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
import com.estacionamento.estacionamento.models.ParkingSpot;
import com.estacionamento.estacionamento.services.ParkingSpotService;

@RestController
@RequestMapping("/parking-spots")
public class ParkingSpotController {
	
	 	@Autowired
	    private ParkingSpotService parkingSpotService;

	 	@GetMapping
		public ResponseEntity<List<ParkingSpotDTO>> getAll() {
			List<ParkingSpot> parkingSpots = parkingSpotService.findAll();
			List<ParkingSpotDTO> dtoList = parkingSpots.stream()
				.map(ParkingSpotDTO::new)
				.collect(Collectors.toList());
			return ResponseEntity.ok(dtoList);
		}

		@GetMapping("/{id}")
		public ResponseEntity<ParkingSpotDTO> getById(@PathVariable Long id) {
			return parkingSpotService.findById(id)
				.map(vaga -> ResponseEntity.ok(new ParkingSpotDTO(vaga)))
				.orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
		}

		@PostMapping
		public ResponseEntity<ParkingSpotDTO> create(@RequestBody ParkingSpotDTO parkingSpotDTO) {
			// Convert DTO to entity and create it
		    System.out.println("Tipo: " + parkingSpotDTO.getTipo());
		    System.out.println("Status: " + parkingSpotDTO.getStatus());
			ParkingSpot vaga = parkingSpotService.criarVaga(parkingSpotDTO.getTipo(), parkingSpotDTO.getStatus());
			return ResponseEntity.status(HttpStatus.CREATED).body(new ParkingSpotDTO(vaga));
		}

		@DeleteMapping("/{id}")
		public ResponseEntity<String> delete(@PathVariable Long id) {
			try {
				parkingSpotService.deleteById(id);
				return ResponseEntity.ok("Vaga excluída com sucesso.");
			} catch (RuntimeException e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
			}
		}

		@PutMapping("/{id}")
		public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ParkingSpotDTO vagaAtualizadaDTO) {
			try {
				// Convert DTO to entity for update
				ParkingSpot vagaAtualizada = new ParkingSpot(vagaAtualizadaDTO.getNumero(), vagaAtualizadaDTO.getTipo(), vagaAtualizadaDTO.getStatus());
				ParkingSpot vaga = parkingSpotService.atualizarVaga(id, vagaAtualizada);
				return ResponseEntity.ok(new ParkingSpotDTO(vaga));
			} catch (RuntimeException e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
			}
		}

}
