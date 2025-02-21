package com.estacionamento.estacionamento.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.estacionamento.estacionamento.models.Customer;
import com.estacionamento.estacionamento.repository.CustomerRepository;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    // Salvar um novo cliente
    public Customer save(Customer customer) {
    	Customer clienteSalvo = customerRepository.save(customer);
        System.out.println("Cliente salvo com ID: " + clienteSalvo.getId()); // Depuração
        return clienteSalvo; // Retorna o cliente já persistido com ID
    }

    // Buscar cliente por ID
    public Customer findById(Long id) {
        return customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Cliente não encontrado!"));
    }

    // Atualizar cliente
    public Customer update(Long id, Customer customerDetails) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado!"));

        existingCustomer.setNome(customerDetails.getNome());

        return customerRepository.save(existingCustomer);
    }

    // Deletar cliente por ID
    public void delete(Long id) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado!"));

        customerRepository.delete(existingCustomer);
    }

    // Buscar todos os clientes
    public Iterable<Customer> findAll() {
        return customerRepository.findAll();
    }
}