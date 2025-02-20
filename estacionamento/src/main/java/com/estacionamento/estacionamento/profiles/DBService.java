package com.estacionamento.estacionamento.profiles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.estacionamento.estacionamento.models.VacancyStatus;
import com.estacionamento.estacionamento.models.VacancyType;
import com.estacionamento.estacionamento.services.ParkingSpotService;

@Service
public class DBService {

	@Autowired
	private ParkingSpotService parkingSpotService;

	public void instanciaDB() {

		 	parkingSpotService.criarVaga(VacancyType.VIP, VacancyStatus.DISPONIVEL);
	        parkingSpotService.criarVaga(VacancyType.COMUM, VacancyStatus.DISPONIVEL);
	        parkingSpotService.criarVaga(VacancyType.DEFICIENTE, VacancyStatus.DISPONIVEL);
	        
	        parkingSpotService.criarVaga(VacancyType.DEFICIENTE, VacancyStatus.OCUPADA);
	        parkingSpotService.criarVaga(VacancyType.VIP, VacancyStatus.RESERVADA);
	    }
		

	}
