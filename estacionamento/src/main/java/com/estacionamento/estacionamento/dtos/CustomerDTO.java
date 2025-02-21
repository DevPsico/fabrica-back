package com.estacionamento.estacionamento.dtos;

import com.estacionamento.estacionamento.models.Customer;

import lombok.Data;

@Data
public class CustomerDTO {
	
    private Long id;
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
