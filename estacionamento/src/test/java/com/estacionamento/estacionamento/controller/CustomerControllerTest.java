package com.estacionamento.estacionamento.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.estacionamento.estacionamento.dtos.CustomerDTO;
import com.estacionamento.estacionamento.exceptions.ClientHasActiveReservationsException;
import com.estacionamento.estacionamento.exceptions.ObjectNotFoundException;
import com.estacionamento.estacionamento.models.Customer;
import com.estacionamento.estacionamento.services.CustomerService;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private Customer customer;
    private CustomerDTO customerDTO;
    
    private Validator validator;

    @BeforeEach
    void setUp() {
        customer = new Customer(1L, "João Silva");
        customerDTO = new CustomerDTO(customer);
        
     // Configura o validador para testes
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // Teste para listar todos os clientes
    @Test
    void getAll_ShouldReturnListOfCustomers() {
        when(customerService.findAll()).thenReturn(Arrays.asList(customer));

        List<CustomerDTO> result = customerController.getAll();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("João Silva", result.get(0).getNome());
    }

    // Teste para buscar um cliente por ID (sucesso)
    @Test
    void getById_ShouldReturnCustomerDTO_WhenCustomerExists() {
        when(customerService.findById(1L)).thenReturn(customer);

        ResponseEntity<CustomerDTO> response = customerController.getById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("João Silva", response.getBody().getNome());
    }

    // Teste para buscar um cliente por ID (cliente não encontrado)
    @Test
    void getById_ShouldThrowException_WhenCustomerDoesNotExist() {
        when(customerService.findById(1L)).thenThrow(new ObjectNotFoundException("Cliente não encontrado com o ID: 1"));

        assertThrows(ObjectNotFoundException.class, () -> {
            customerController.getById(1L);
        });
    }

    // Teste para criar um cliente (sucesso)
    @Test
    void create_ShouldReturnCreatedCustomerDTO() {
        when(customerService.save(any(Customer.class))).thenReturn(customer);

        ResponseEntity<CustomerDTO> response = customerController.create(customerDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("João Silva", response.getBody().getNome());
    }

  



    // Teste para atualizar um cliente (sucesso)
    @Test
    void update_ShouldReturnUpdatedCustomerDTO() {
        when(customerService.findById(1L)).thenReturn(customer);
        when(customerService.save(any(Customer.class))).thenReturn(customer);

        ResponseEntity<CustomerDTO> response = customerController.update(1L, customerDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("João Silva", response.getBody().getNome());
    }

    // Teste para atualizar um cliente (cliente não encontrado)
    @Test
    void update_ShouldThrowException_WhenCustomerDoesNotExist() {
        when(customerService.findById(1L)).thenThrow(new ObjectNotFoundException("Cliente não encontrado com o ID: 1"));

        assertThrows(ObjectNotFoundException.class, () -> {
            customerController.update(1L, customerDTO);
        });
    }

    // Teste para deletar um cliente (sucesso)
    @Test
    void delete_ShouldReturnNoContent() {
        doNothing().when(customerService).delete(1L);

        ResponseEntity<Void> response = customerController.delete(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    // Teste para deletar um cliente (cliente não encontrado)
    @Test
    void delete_ShouldThrowException_WhenCustomerDoesNotExist() {
        doThrow(new ObjectNotFoundException("Cliente com ID 1 não encontrado."))
                .when(customerService).delete(1L);

        assertThrows(ObjectNotFoundException.class, () -> {
            customerController.delete(1L);
        });
    }

    // Teste para deletar um cliente (cliente com reservas ativas)
    @Test
    void delete_ShouldThrowException_WhenCustomerHasActiveReservations() {
        doThrow(new ClientHasActiveReservationsException("Cliente possui reservas ativas e não pode ser excluído."))
                .when(customerService).delete(1L);

        assertThrows(ClientHasActiveReservationsException.class, () -> {
            customerController.delete(1L);
        });
    }
}