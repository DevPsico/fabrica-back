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

@RestController
@RequestMapping("/clientes")  // Definindo a URL base para o controller
@Validated
public class CustomerController {

    @Autowired
    private CustomerService customerService;

 // Endpoint para listar todos os clientes
    @GetMapping
    public List<CustomerDTO> getAll() {
        return customerService.findAll()
                .stream()
                .map(CustomerDTO::new)  // Converte cada entidade para DTO
                .collect(Collectors.toList());
    }

    // Endpoint para buscar um cliente por ID
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getById(@PathVariable Long id) {
        // Busca o cliente pelo ID (se não existir, a exceção será lançada)
        Customer customer = customerService.findById(id);

        // Converte o cliente para DTO e retorna
        return ResponseEntity.ok(new CustomerDTO(customer));
    }

    // Endpoint para criar um novo cliente
    @PostMapping
    public ResponseEntity<CustomerDTO> create(@RequestBody CustomerDTO customerDTO) {
        // Validação do DTO
        if (customerDTO == null) {
            throw new IllegalArgumentException("O corpo da requisição não pode ser nulo.");
        }
        if (customerDTO.getNome() == null || customerDTO.getNome().isEmpty()) {
            throw new IllegalArgumentException("O campo 'nome' é obrigatório.");
        }

        // Converte o DTO para a entidade Customer
        Customer customer = new Customer();
        customer.setNome(customerDTO.getNome());

        // Salva o cliente no banco de dados
        Customer savedCustomer = customerService.save(customer);

        // Converte a entidade salva de volta para DTO
        CustomerDTO responseDTO = new CustomerDTO(savedCustomer);

        // Retorna a resposta com status 201 (Created) e o DTO no corpo
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    // Endpoint para atualizar um cliente existente
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> update(@PathVariable Long id, @RequestBody CustomerDTO customerDTO) {
        // Validação do DTO
        if (customerDTO == null) {
            throw new IllegalArgumentException("O corpo da requisição não pode ser nulo.");
        }
        if (customerDTO.getNome() == null || customerDTO.getNome().isEmpty()) {
            throw new IllegalArgumentException("O campo 'nome' é obrigatório.");
        }

        // Busca o cliente existente pelo ID
        Customer existingCustomer = customerService.findById(id);

        // Atualiza os dados do cliente
        existingCustomer.setNome(customerDTO.getNome());

        // Salva o cliente atualizado no banco de dados
        Customer updatedCustomer = customerService.save(existingCustomer);

        // Converte a entidade atualizada de volta para DTO
        CustomerDTO responseDTO = new CustomerDTO(updatedCustomer);

        // Retorna a resposta com status 200 (OK) e o DTO no corpo
        return ResponseEntity.ok(responseDTO);
    }

    // Endpoint para deletar um cliente por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        // Deleta o cliente pelo ID
        customerService.delete(id);

        // Retorna status 204 (NO_CONTENT) para indicar sucesso na exclusão
        return ResponseEntity.noContent().build();
    } 
}