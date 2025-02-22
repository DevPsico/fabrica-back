package com.estacionamento.estacionamento.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import com.estacionamento.estacionamento.services.CustomerService;
import com.estacionamento.estacionamento.models.Customer;
import com.estacionamento.estacionamento.repository.CustomerRepository;
import com.estacionamento.estacionamento.repository.ReservationRepository;
import com.estacionamento.estacionamento.exceptions.ObjectNotFoundException;
import com.estacionamento.estacionamento.exceptions.ClientHasActiveReservationsException;
import com.estacionamento.estacionamento.exceptions.NoCustomersFoundException;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customer = new Customer();
        customer.setId(1L);
        customer.setNome("João");
    }

    /**
     * Testa se um cliente é salvo corretamente no repositório.
     */
    @Test
    void testSaveCustomer() {
        when(customerRepository.save(customer)).thenReturn(customer);
        Customer savedCustomer = customerService.save(customer);
        assertNotNull(savedCustomer);
        assertEquals("João", savedCustomer.getNome());
    }

    /**
     * Testa se um cliente pode ser encontrado pelo ID quando ele existe.
     */
    @Test
    void testFindByIdSuccess() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        Customer foundCustomer = customerService.findById(1L);
        assertNotNull(foundCustomer);
        assertEquals(1L, foundCustomer.getId());
    }

    /**
     * Testa se uma exceção é lançada quando um cliente não é encontrado pelo ID.
     */
    @Test
    void testFindByIdNotFound() {
        when(customerRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ObjectNotFoundException.class, () -> customerService.findById(2L));
    }

    /**
     * Testa se a atualização de um cliente existente ocorre corretamente.
     */
    @Test
    void testUpdateCustomerSuccess() {
        Customer updatedCustomer = new Customer();
        updatedCustomer.setNome("Carlos");
        
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomer);
        
        Customer result = customerService.update(1L, updatedCustomer);
        assertNotNull(result);
        assertEquals("Carlos", result.getNome());
    }

    /**
     * Testa se uma exceção é lançada ao tentar atualizar um cliente inexistente.
     */
    @Test
    void testUpdateCustomerNotFound() {
        Customer updatedCustomer = new Customer();
        updatedCustomer.setNome("Carlos");
        
        when(customerRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ObjectNotFoundException.class, () -> customerService.update(2L, updatedCustomer));
    }

    /**
     * Testa se um cliente pode ser deletado com sucesso quando não possui reservas ativas.
     */
    @Test
    void testDeleteCustomerSuccess() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(reservationRepository.existsByClienteAndDataFimIsNull(customer)).thenReturn(false);
        
        assertDoesNotThrow(() -> customerService.delete(1L));
        verify(customerRepository, times(1)).delete(customer);
    }

    /**
     * Testa se uma exceção é lançada ao tentar excluir um cliente com reservas ativas.
     */
    @Test
    void testDeleteCustomerWithActiveReservations() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(reservationRepository.existsByClienteAndDataFimIsNull(customer)).thenReturn(true);
        
        assertThrows(ClientHasActiveReservationsException.class, () -> customerService.delete(1L));
    }

    /**
     * Testa se a busca por todos os clientes retorna corretamente uma lista de clientes.
     */
    @Test
    void testFindAllCustomersSuccess() {
        List<Customer> customers = Arrays.asList(new Customer(1L, "João"), new Customer(2L, "Maria"));
        when(customerRepository.findAll()).thenReturn(customers);
        List<Customer> result = customerService.findAll();
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
    }

    /**
     * Testa se uma exceção é lançada quando não há clientes cadastrados no sistema.
     */
    @Test
    void testFindAllCustomersEmpty() {
        when(customerRepository.findAll()).thenReturn(Arrays.asList());
        assertThrows(NoCustomersFoundException.class, () -> customerService.findAll());
    }
}