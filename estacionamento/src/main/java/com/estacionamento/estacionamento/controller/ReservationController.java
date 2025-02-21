package com.estacionamento.estacionamento.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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

import com.estacionamento.estacionamento.dtos.ReservationDTO;
import com.estacionamento.estacionamento.models.Reservation;
import com.estacionamento.estacionamento.services.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
	
	@Autowired
    private ReservationService reservationService;

	 @GetMapping
	    public List<ReservationDTO> getAll() {
	        return reservationService.findAll()
	                .stream()
	                .map(ReservationDTO::new) // Converte cada entidade para DTO
	                .collect(Collectors.toList());
	    }

	 @GetMapping("/{id}")
	 public ResponseEntity<ReservationDTO> getById(@PathVariable Long id) {
	     Optional<Reservation> reservationOpt = reservationService.findById(id);

	     if (reservationOpt.isPresent()) {
	         // Se encontrar a reserva, retorna com o DTO
	         Reservation reservation = reservationOpt.get();
	         return ResponseEntity.ok(new ReservationDTO(reservation));
	     } else {
	         // Caso não encontre, retorna 404
	         return ResponseEntity.notFound().build();
	     }
	 }

	 @PostMapping
	 public ResponseEntity<ReservationDTO> create(@RequestBody ReservationDTO reservationDTO) {
	     // Passando os parâmetros corretos para o método create
	     Long parkingSpotId = reservationDTO.getParkingSpot().getId();
	     Long customerId = reservationDTO.getCliente().getId();
	     LocalDateTime dataInicio = reservationDTO.getDataInicio();

	     // Criando a reserva
	     Reservation savedReservation = reservationService.create(parkingSpotId, customerId, dataInicio);

	     // Retornando a resposta com a reserva criada
	     return ResponseEntity.status(HttpStatus.CREATED).body(new ReservationDTO(savedReservation));
	 }  
	 
	// Endpoint para finalizar uma reserva
	    @PutMapping("/{id}")
	    public ResponseEntity<ReservationDTO> finalizarReserva(@PathVariable Long id, @RequestBody ReservationDTO reservationDTO) {
	        LocalDateTime dataFim = reservationDTO.getDataFim();
	        
	        if (dataFim == null) {
	            return ResponseEntity.badRequest().build();  // Se dataFim não for fornecida, retorna erro 400
	        }

	        try {
	            Reservation updatedReservation = reservationService.finalizarReserva(id, dataFim);
	            return ResponseEntity.ok(new ReservationDTO(updatedReservation));
	        } catch (RuntimeException e) {
	            return ResponseEntity.badRequest().body(null);  // Caso erro na finalização
	        }
	    }
}