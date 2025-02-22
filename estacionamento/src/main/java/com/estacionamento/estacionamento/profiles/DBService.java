package com.estacionamento.estacionamento.profiles;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.estacionamento.estacionamento.exceptions.ParkingSpotNotAvailableException;
import com.estacionamento.estacionamento.models.Customer;
import com.estacionamento.estacionamento.models.ParkingSpot;
import com.estacionamento.estacionamento.models.VacancyStatus;
import com.estacionamento.estacionamento.models.VacancyType;
import com.estacionamento.estacionamento.services.CustomerService;
import com.estacionamento.estacionamento.services.ParkingSpotService;
import com.estacionamento.estacionamento.services.ReservationService;

@Service
public class DBService {

	@Autowired
	private ParkingSpotService parkingSpotService;

	@Autowired
	private ReservationService reservationService;

	@Autowired
	private CustomerService customerService;

	public void instanciaDB() throws ParkingSpotNotAvailableException {

		// Criar e salvar clientes utilizando o serviço
		Customer cliente1 = customerService.save(new Customer("Cliente 1"));
		Customer cliente2 = customerService.save(new Customer("Cliente 2"));
		Customer cliente3 = customerService.save(new Customer("Oii"));
		Customer cliente4 = customerService.save(new Customer("cliente 4"));

		
		// Criar vagas
		ParkingSpot vaga1 = parkingSpotService.criarVaga(VacancyType.VIP, VacancyStatus.DISPONIVEL);
		ParkingSpot vaga2 = parkingSpotService.criarVaga(VacancyType.COMUM, VacancyStatus.DISPONIVEL);
		ParkingSpot vaga3 = parkingSpotService.criarVaga(VacancyType.DEFICIENTE, VacancyStatus.DISPONIVEL);
		ParkingSpot vaga4 = parkingSpotService.criarVaga(VacancyType.DEFICIENTE, VacancyStatus.DISPONIVEL);
		ParkingSpot vaga5 = parkingSpotService.criarVaga(VacancyType.VIP, VacancyStatus.DISPONIVEL);

		// Criar reservas
		reservationService.create(vaga1.getId(), cliente1.getId(), LocalDateTime.now()); // Reserva vaga 1
		reservationService.create(vaga2.getId(), cliente2.getId(), LocalDateTime.now()); // Reserva vaga 2
		reservationService.create(vaga3.getId(), cliente3.getId(), LocalDateTime.now()); // Reserva vaga 3
		
	}
}