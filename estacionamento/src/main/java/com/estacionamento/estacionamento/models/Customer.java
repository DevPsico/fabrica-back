package com.estacionamento.estacionamento.models;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
public class Customer {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "O nome do cliente não pode estar em branco.")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
    private String nome;

  
    @JsonManagedReference
    @OneToMany(mappedBy = "cliente")
    private List<Reservation> reservas;

    // Construtores, getters e setters
    public Customer() {
    }
   
    public Customer (Long id) {
    	this.id = id;
    }

	public Customer(Long id, String nome) {
	
		this.id = id;
		this.nome = nome;
		
	}public Customer( String nome) {
	
		this.nome = nome;
		
	}
	
}