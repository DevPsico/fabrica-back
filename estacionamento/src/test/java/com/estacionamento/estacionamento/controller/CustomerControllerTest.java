package com.estacionamento.estacionamento.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.estacionamento.estacionamento.dtos.CustomerDTO;
import com.estacionamento.estacionamento.models.Customer;
import com.estacionamento.estacionamento.services.CustomerService;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private Customer customer;
    private CustomerDTO customerDTO;

    @BeforeEach
    void setUp() {
        customer = new Customer(1L, "João Silva");
        customerDTO = new CustomerDTO(customer);
    }

    @Test
    void getAll_ShouldReturnListOfCustomers() {
        when(customerService.findAll()).thenReturn(Arrays.asList(customer));
        
        List<CustomerDTO> result = customerController.getAll();
        
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("João Silva", result.get(0).getNome());
    }

    @Test
    void getById_ShouldReturnCustomerDTO_WhenCustomerExists() {
        when(customerService.findById(1L)).thenReturn(customer);
        
        ResponseEntity<CustomerDTO> response = customerController.getById(1L);
        
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("João Silva", response.getBody().getNome());
    }

    @Test
    void create_ShouldReturnCreatedCustomerDTO() {
        when(customerService.save(any(Customer.class))).thenReturn(customer);
        
        ResponseEntity<CustomerDTO> response = customerController.create(customerDTO);
        
        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("João Silva", response.getBody().getNome());
    }

    @Test
    void update_ShouldReturnUpdatedCustomerDTO() {
        when(customerService.findById(1L)).thenReturn(customer);
        when(customerService.save(any(Customer.class))).thenReturn(customer);
        
        ResponseEntity<CustomerDTO> response = customerController.update(1L, customerDTO);
        
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("João Silva", response.getBody().getNome());
    }

    @Test
    void delete_ShouldReturnNoContent() {
        doNothing().when(customerService).delete(1L);
        
        ResponseEntity<Void> response = customerController.delete(1L);
        
        assertEquals(204, response.getStatusCodeValue());
    }
}

