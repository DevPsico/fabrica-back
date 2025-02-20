package com.estacionamento.estacionamento.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.estacionamento.estacionamento.models.ParkingSpot;
import com.estacionamento.estacionamento.models.VacancyType;

@Repository
public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Long>{
	
	 @Query("SELECT MAX(p.numero) FROM ParkingSpot p WHERE p.tipo = :tipo")
	    String findUltimoNumeroPorTipo(VacancyType tipo);

}
