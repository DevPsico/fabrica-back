package com.estacionamento.estacionamento.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.estacionamento.estacionamento.models.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>{

}
