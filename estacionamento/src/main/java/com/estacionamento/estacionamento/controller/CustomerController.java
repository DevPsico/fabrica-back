package com.estacionamento.estacionamento.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.estacionamento.estacionamento.models.Customer;
import com.estacionamento.estacionamento.services.CustomerService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/clientes")  // Definindo a URL base para o controller
@Validated
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // Endpoint para criar um novo cliente
    @PostMapping
    public ResponseEntity<Customer> create(@Valid @RequestBody Customer customer) {
        Customer savedCustomer = customerService.save(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer);
    }

    // Endpoint para buscar todos os clientes
    @GetMapping
    public ResponseEntity<Iterable<Customer>> getAll() {
        Iterable<Customer> customers = customerService.findAll();
        return ResponseEntity.ok(customers);  // Retorna todos os clientes com status 200
    }

    // Endpoint para buscar um cliente por ID
    @GetMapping("/{id}")
    public ResponseEntity<Customer> findById(@PathVariable Long id) {
        Customer customer = customerService.findById(id);
        return ResponseEntity.ok(customer);
    }
    

    // Endpoint para atualizar um cliente existente
    @PutMapping("/{id}")
    public ResponseEntity<Customer> update(@PathVariable Long id, @RequestBody Customer customerDetails) {
        Customer updatedCustomer = customerService.update(id, customerDetails);
        return ResponseEntity.ok(updatedCustomer);
    }

 // Endpoint para deletar um cliente por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build(); // Retorna 204 se excluir com sucesso
    }
}