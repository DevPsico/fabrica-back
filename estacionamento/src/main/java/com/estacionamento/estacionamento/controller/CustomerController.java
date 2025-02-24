package com.estacionamento.estacionamento.controller;

import java.util.List;
import java.util.stream.Collectors;

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

import com.estacionamento.estacionamento.dtos.CustomerDTO;
import com.estacionamento.estacionamento.models.Customer;
import com.estacionamento.estacionamento.services.CustomerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/clientes")
@Validated
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // Endpoint para listar todos os clientes
    @GetMapping
    public List<CustomerDTO> getAll() {
        return customerService.findAll()
                .stream()
                .map(CustomerDTO::new) // Converte cada entidade para DTO
                .collect(Collectors.toList());
    }

    // Endpoint para buscar um cliente por ID
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getById(@PathVariable Long id) {
        Customer customer = customerService.findById(id);
        return ResponseEntity.ok(new CustomerDTO(customer));
    }

    // Endpoint para criar um novo cliente
    @PostMapping
    public ResponseEntity<CustomerDTO> create(@Valid @RequestBody CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setNome(customerDTO.getNome());

        Customer savedCustomer = customerService.save(customer);
        CustomerDTO responseDTO = new CustomerDTO(savedCustomer);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    // Endpoint para atualizar um cliente existente
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> update(@PathVariable Long id, @Valid @RequestBody CustomerDTO customerDTO) {
        Customer existingCustomer = customerService.findById(id);
        existingCustomer.setNome(customerDTO.getNome());

        Customer updatedCustomer = customerService.save(existingCustomer);
        CustomerDTO responseDTO = new CustomerDTO(updatedCustomer);

        return ResponseEntity.ok(responseDTO);
    }

    // Endpoint para deletar um cliente por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}