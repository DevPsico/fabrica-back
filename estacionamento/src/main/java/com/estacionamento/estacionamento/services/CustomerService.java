package com.estacionamento.estacionamento.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.estacionamento.estacionamento.exceptions.ClientHasActiveReservationsException;
import com.estacionamento.estacionamento.exceptions.NoCustomersFoundException;
import com.estacionamento.estacionamento.exceptions.ObjectNotFoundException;
import com.estacionamento.estacionamento.models.Customer;
import com.estacionamento.estacionamento.repository.CustomerRepository;
import com.estacionamento.estacionamento.repository.ReservationRepository;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    // Salvar um novo cliente
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    // Buscar cliente por ID
    public Customer findById(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        return customer.orElseThrow(() -> new ObjectNotFoundException("Cliente não encontrado com o ID: " + id));
    }

    // Atualizar cliente
    public Customer update(Long id, Customer customerDetails) {
        // Verifica se o cliente existe, senão lança exceção 404
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Cliente não encontrado com o ID: " + id));

        // Atualiza os dados do cliente
        existingCustomer.setNome(customerDetails.getNome());

        return customerRepository.save(existingCustomer);
    }

    // Deletar cliente
    public void delete(Long id) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Cliente com ID " + id + " não encontrado."));

        // Verifica se o cliente possui reservas ativas
        boolean hasActiveReservations = reservationRepository.existsByClienteAndDataFimIsNull(existingCustomer);

        if (hasActiveReservations) {
            throw new ClientHasActiveReservationsException("Cliente possui reservas ativas e não pode ser excluído.");
        }

        customerRepository.delete(existingCustomer);
    }

    // Buscar todos os clientes
    public List<Customer> findAll() {
        List<Customer> customers = customerRepository.findAll();
        if (customers.isEmpty()) {
            throw new NoCustomersFoundException("Nenhum cliente encontrado.");
        }
        return customers;
    }
}