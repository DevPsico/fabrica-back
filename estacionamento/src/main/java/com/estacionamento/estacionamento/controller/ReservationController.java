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
import com.estacionamento.estacionamento.exceptions.InvalidDataFimException;
import com.estacionamento.estacionamento.exceptions.ParkingSpotNotAvailableException;
import com.estacionamento.estacionamento.exceptions.ReservationAlreadyFinalizedException;
import com.estacionamento.estacionamento.exceptions.ReservationNotFoundException;
import com.estacionamento.estacionamento.exceptions.StandardErro;
import com.estacionamento.estacionamento.exceptions.ValidationError;
import com.estacionamento.estacionamento.models.Reservation;
import com.estacionamento.estacionamento.services.ReservationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

	@Autowired
	private ReservationService reservationService;

	@GetMapping
	public List<ReservationDTO> getAll() {
		return reservationService.findAll().stream().map(ReservationDTO::new) // Converte cada entidade para DTO
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
	public ResponseEntity<ReservationDTO> create(@Valid @RequestBody ReservationDTO reservationDTO)
	        throws ParkingSpotNotAvailableException {
	    // Verifica se o parkingSpot é nulo
	    if (reservationDTO.getParkingSpot() == null) {
	        throw new IllegalArgumentException("O campo 'parkingSpot' não pode ser nulo.");
	    }

	    // Verifica se o cliente é nulo
	    if (reservationDTO.getCliente() == null) {
	        throw new IllegalArgumentException("O campo 'cliente' não pode ser nulo.");
	    }

	    // Extrai os dados do DTO
	    Long parkingSpotId = reservationDTO.getParkingSpot().getId();
	    Long customerId = reservationDTO.getCliente().getId();
	    LocalDateTime dataInicio = reservationDTO.getDataInicio();

	    // Verifica se o ID do cliente é nulo
	    if (customerId == null) {
	        throw new IllegalArgumentException("O ID do cliente não pode ser nulo.");
	    }

	    // Cria a reserva
	    Reservation savedReservation = reservationService.create(parkingSpotId, customerId, dataInicio);

	    // Verifica se a reserva foi criada corretamente
	    if (savedReservation == null) {
	        throw new IllegalStateException("Falha ao criar a reserva.");
	    }

	    // Converte a reserva salva para DTO
	    ReservationDTO responseDTO = new ReservationDTO(savedReservation);

	    // Retorna a resposta com status 201 (Created) e o DTO no corpo
	    return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> finalizarReserva(@PathVariable Long id, @Valid @RequestBody ReservationDTO reservationDTO) {
	    try {
	        // Validação do ID
	        if (id <= 0) {
	            throw new IllegalArgumentException("O ID da reserva deve ser um valor positivo.");
	        }

	        LocalDateTime dataFim = reservationDTO.getDataFim();

	        // Validação do campo dataFim
	        if (dataFim == null) {
	            throw new IllegalArgumentException("O campo 'dataFim' é obrigatório.");
	        }

	        // Validação para garantir que dataFim não seja igual a dataInicio
	        if (dataFim.isEqual(reservationDTO.getDataInicio())) {
	            throw new IllegalArgumentException("A data de fim não pode ser igual à data de início.");
	        }

	        // Finaliza a reserva
	        Reservation updatedReservation = reservationService.finalizarReserva(id, dataFim);
	        return ResponseEntity.ok(new ReservationDTO(updatedReservation)); // Retorna 200 com a reserva atualizada

	    } catch (IllegalArgumentException ex) {
	        // Retorna 400 com uma mensagem de erro no corpo
	        ValidationError validationError = new ValidationError(
	            LocalDateTime.now(), // Timestamp do erro
	            HttpStatus.BAD_REQUEST.value(), // Código HTTP (400)
	            "Erro de validação", // Mensagem geral
	            "/reservations/" + id // Caminho da requisição
	        );
	        validationError.addErros("dataFim", ex.getMessage());
	        return ResponseEntity.badRequest().body(validationError);

	    } catch (ReservationNotFoundException ex) {
	        // Retorna 404 com uma mensagem de erro no corpo
	        StandardErro errorResponse = new StandardErro(
	            LocalDateTime.now(), // Timestamp do erro
	            HttpStatus.NOT_FOUND.value(), // Código HTTP (404)
	            ex.getMessage(), // Mensagem da exceção
	            "/reservations/" + id // Caminho da requisição
	        );
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);

	    } catch (ReservationAlreadyFinalizedException ex) {
	        // Retorna 400 com uma mensagem de erro no corpo
	        StandardErro errorResponse = new StandardErro(
	            LocalDateTime.now(), // Timestamp do erro
	            HttpStatus.BAD_REQUEST.value(), // Código HTTP (400)
	            ex.getMessage(), // Mensagem da exceção
	            "/reservations/" + id // Caminho da requisição
	        );
	        return ResponseEntity.badRequest().body(errorResponse);

	    } catch (Exception ex) {
	        // Retorna 500 com uma mensagem de erro no corpo
	        StandardErro errorResponse = new StandardErro(
	            LocalDateTime.now(), // Timestamp do erro
	            HttpStatus.INTERNAL_SERVER_ERROR.value(), // Código HTTP (500)
	            "Erro inesperado: " + ex.getMessage(), // Mensagem da exceção
	            "/reservations/" + id // Caminho da requisição
	        );
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	    }
	}
}