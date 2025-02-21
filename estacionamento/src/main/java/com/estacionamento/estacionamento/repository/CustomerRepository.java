package com.estacionamento.estacionamento.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.estacionamento.estacionamento.models.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // Aqui você pode adicionar métodos personalizados, se necessário
}
