package com.estacionamento.estacionamento.dtos;

import com.estacionamento.estacionamento.models.Customer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerDTO {
	
    private Long id;
    
    @NotBlank(message = "O campo 'nome' é obrigatório.")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
    private String nome;


    // Construtores
    public CustomerDTO() {
    }

    public CustomerDTO(Long id, String nome) {
        this.id = id;
        this.nome = nome;
   
    }

    // Construtor para converter o modelo Customer em DTO
    public CustomerDTO(Customer customer) {
        this.id = customer.getId();
        this.nome = customer.getNome();
    }

}
