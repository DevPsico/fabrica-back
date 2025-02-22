package com.estacionamento.estacionamento.exceptions;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalException {
	
	@ExceptionHandler(ObjectNotFoundException.class)
	public ResponseEntity<StandardErro> handleObjectNotFound(ObjectNotFoundException ex, HttpServletRequest request) {
	    // Cria um objeto StandardErro com os detalhes do erro
	    StandardErro error = new StandardErro(
	        LocalDateTime.now(), // Timestamp do erro
	        HttpStatus.NOT_FOUND.value(), // Código HTTP (404)
	        ex.getMessage(), // Mensagem da exceção
	        request.getRequestURI() // Caminho da requisição que causou o erro
	    );

	    // Retorna a resposta com status 404 e o corpo contendo o objeto StandardErro
	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	@ExceptionHandler(ClientHasActiveReservationsException.class)
	public ResponseEntity<StandardErro> handleClientHasActiveReservations(ClientHasActiveReservationsException ex, HttpServletRequest request) {
	
	    StandardErro error = new StandardErro(
	        LocalDateTime.now(),
	        HttpStatus.BAD_REQUEST.value(),
	        ex.getMessage(),
	        request.getRequestURI()
	    );

	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<StandardErro> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
	    StandardErro error = new StandardErro(
	        LocalDateTime.now(),
	        HttpStatus.BAD_REQUEST.value(),
	        ex.getMessage(),
	        request.getRequestURI()
	    );

	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationError> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
	    ValidationError error = new ValidationError(
	        LocalDateTime.now(),
	        HttpStatus.BAD_REQUEST.value(),
	        "Erro de validação",
	        request.getRequestURI()
	    );

	    for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
	        error.addErros(fieldError.getField(), fieldError.getDefaultMessage());
	    }

	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
	
	@ExceptionHandler(ParkingSpotNotAvailableException.class)
	public ResponseEntity<StandardErro> handleParkingSpotNotAvailablEntity(ParkingSpotNotAvailableException ex, HttpServletRequest request) {
	    StandardErro error = new StandardErro(
	        LocalDateTime.now(),
	        HttpStatus.BAD_REQUEST.value(),
	        ex.getMessage(),
	        request.getRequestURI()
	    );
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
	
	@ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<StandardErro> handleNoResourceFound(NoResourceFoundException ex, HttpServletRequest request) {
        StandardErro error = new StandardErro(
            LocalDateTime.now(),
            HttpStatus.NOT_FOUND.value(),
            "Endpoint não encontrado: " + request.getRequestURI(),
            request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<StandardErro> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
	    String mensagemErro = "Erro na leitura do JSON. Verifique os campos enviados.";

	    // Verifica se a exceção foi causada por um campo inválido (como um enum)
	    if (ex.getCause() instanceof InvalidFormatException) {
	        InvalidFormatException cause = (InvalidFormatException) ex.getCause();
	        Class<?> targetType = cause.getTargetType();

	        // Verifica se o tipo alvo é um enum
	        if (targetType.isEnum()) {
	            // Obtém os valores do enum e os formata como uma lista legível
	            Object[] enumValues = targetType.getEnumConstants();
	            String valoresEsperados = Arrays.stream(enumValues)
	                                            .map(Object::toString)
	                                            .collect(Collectors.joining(", ", "[", "]"));

	            mensagemErro = "Valor inválido para o campo '" + cause.getPath().get(0).getFieldName() + "'. Valores esperados: " + valoresEsperados;
	        } else {
	            mensagemErro = "Valor inválido para o campo '" + cause.getPath().get(0).getFieldName() + "'.";
	        }
	    }

	    StandardErro error = new StandardErro(
	        LocalDateTime.now(),
	        HttpStatus.BAD_REQUEST.value(),
	        mensagemErro,
	        request.getRequestURI()
	    );
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<StandardErro> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
	    String mensagemErro = "Valor inválido para o parâmetro '" + ex.getName() + "'. O valor deve ser um número.";

	    StandardErro error = new StandardErro(
	        LocalDateTime.now(),
	        HttpStatus.BAD_REQUEST.value(),
	        mensagemErro,
	        request.getRequestURI()
	    );
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
	
	@ExceptionHandler(NoReservationsFoundException.class)
	public ResponseEntity<StandardErro> handleNoReservationsFound(NoReservationsFoundException ex, HttpServletRequest request) {
	    StandardErro error = new StandardErro(
	        LocalDateTime.now(),
	        HttpStatus.NOT_FOUND.value(),
	        ex.getMessage(),
	        request.getRequestURI()
	    );
	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	
	@ExceptionHandler(ReservationNotFoundException.class)
	public ResponseEntity<StandardErro> handleReservationNotFound(ReservationNotFoundException ex, HttpServletRequest request) {
	    StandardErro error = new StandardErro(
	        LocalDateTime.now(),
	        HttpStatus.NOT_FOUND.value(),
	        ex.getMessage(),
	        request.getRequestURI()
	    );
	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	
	@ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<StandardErro> handleCustomerNotFound(CustomerNotFoundException ex, HttpServletRequest request) {
        StandardErro error = new StandardErro(
            LocalDateTime.now(),
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage(),
            request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
	
	@ExceptionHandler(ParkingSpotNotFoundException.class)
	public ResponseEntity<StandardErro> handleParkingSpotNotFound(ParkingSpotNotFoundException ex, HttpServletRequest request) {
	    StandardErro error = new StandardErro(
	        LocalDateTime.now(),
	        HttpStatus.NOT_FOUND.value(),
	        ex.getMessage(),
	        request.getRequestURI()
	    );
	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	
	@ExceptionHandler(NoCustomersFoundException.class)
	public ResponseEntity<StandardErro> handleNoCustomersFound(NoCustomersFoundException ex, HttpServletRequest request) {
	    StandardErro error = new StandardErro(
	        LocalDateTime.now(),
	        HttpStatus.NOT_FOUND.value(),
	        ex.getMessage(),
	        request.getRequestURI()
	    );
	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	
}
