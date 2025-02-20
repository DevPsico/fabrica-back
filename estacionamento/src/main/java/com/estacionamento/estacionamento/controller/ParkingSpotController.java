package com.estacionamento.estacionamento.controller;

import java.util.List;

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

@RestController
@RequestMapping("/parking-spots")
public class ParkingSpotController {
	
	 	@Autowired
	    private ParkingSpotService parkingSpotService;

	    @GetMapping
	    public ResponseEntity<List<ParkingSpot>> getAll() {
	        List<ParkingSpot> parkingSpots = parkingSpotService.findAll();
	        return ResponseEntity.ok(parkingSpots); // Retorna a lista com status 200 OK
	    }

	    @GetMapping("/{id}")
	    public ResponseEntity<ParkingSpot> getById(@PathVariable Long id) {
	        return parkingSpotService.findById(id)
	            .map(vaga -> ResponseEntity.ok(vaga))
	            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	    }

	    @PostMapping
	    public ResponseEntity<ParkingSpot> create(@RequestBody ParkingSpot parking) {
	        // Utiliza o tipo e o status da vaga para criar
	        ParkingSpot vaga = parkingSpotService.criarVaga(parking.getTipo(), parking.getStatus());
	        return ResponseEntity.status(HttpStatus.CREATED).body(vaga);
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
	    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ParkingSpot vagaAtualizada) {
	        try {
	            ParkingSpot vaga = parkingSpotService.atualizarVaga(id, vagaAtualizada);
	            return ResponseEntity.ok(vaga);
	        } catch (RuntimeException e) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	        }
	    }

}
