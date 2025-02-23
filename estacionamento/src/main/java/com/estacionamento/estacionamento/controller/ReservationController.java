package com.estacionamento.estacionamento.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.estacionamento.estacionamento.dtos.ReservationDTO;
import com.estacionamento.estacionamento.exceptions.ParkingSpotNotAvailableException;
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
	     // Busca a reserva pelo ID (se não existir, a exceção será lançada)
	     Reservation reservation = reservationService.findById(id);

	     // Converte a reserva para DTO e retorna
	     return ResponseEntity.ok(new ReservationDTO(reservation));
	 }

	 @PostMapping
	 public ResponseEntity<ReservationDTO> create(@RequestBody ReservationDTO reservationDTO) throws ParkingSpotNotAvailableException {
	     // Validação do DTO
	     if (reservationDTO == null) {
	         throw new IllegalArgumentException("O corpo da requisição não pode ser nulo.");
	     }
	     if (reservationDTO.getParkingSpot() == null || reservationDTO.getCliente() == null) {
	         throw new IllegalArgumentException("Os campos 'parkingSpot' e 'cliente' são obrigatórios.");
	     }
	     if (reservationDTO.getParkingSpot().getId() == null || reservationDTO.getCliente().getId() == null) {
	         throw new IllegalArgumentException("Os IDs da vaga e do cliente são obrigatórios.");
	     }

	     // Extrai os dados do DTO
	     Long parkingSpotId = reservationDTO.getParkingSpot().getId();
	     Long customerId = reservationDTO.getCliente().getId();
	     LocalDateTime dataInicio = reservationDTO.getDataInicio();

	     // Cria a reserva
	     Reservation savedReservation = reservationService.create(parkingSpotId, customerId, dataInicio);

	     // Converte a reserva salva para DTO
	     ReservationDTO responseDTO = new ReservationDTO(savedReservation);

	     // Retorna a resposta com status 201 (Created) e o DTO no corpo
	     return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
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