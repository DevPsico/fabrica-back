package com.estacionamento.estacionamento.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.estacionamento.estacionamento.exceptions.ObjectNotFoundException;
import com.estacionamento.estacionamento.exceptions.ParkingSpotNotFoundException;
import com.estacionamento.estacionamento.models.ParkingSpot;
import com.estacionamento.estacionamento.models.VacancyStatus;
import com.estacionamento.estacionamento.models.VacancyType;
import com.estacionamento.estacionamento.repository.ParkingSpotRepository;

@Service
public class ParkingSpotService {

	@Autowired
	private ParkingSpotRepository parkingSpotRepository;


	public ParkingSpot criarVaga(VacancyType tipo, VacancyStatus status) {
	    if (tipo == null || status == null) {
	        throw new IllegalArgumentException("Tipo e status são obrigatórios.");
	    }
	    
	    String numero = gerarNumeroVaga(tipo);
	    ParkingSpot vaga = new ParkingSpot(numero, tipo, status);
	    return parkingSpotRepository.save(vaga);
	}

	public ParkingSpot findById(Long id) {
        Optional<ParkingSpot> parkingSpot = parkingSpotRepository.findById(id);
        return parkingSpot.orElseThrow(() -> new ParkingSpotNotFoundException("Vaga de estacionamento não encontrada com o ID: " + id));
    }

	public void deleteById(Long id) {
	    if (!parkingSpotRepository.existsById(id)) {
	        throw new ParkingSpotNotFoundException("Vaga não encontrada com o ID: " + id);
	    }
	    parkingSpotRepository.deleteById(id);
	}

	public ParkingSpot atualizarVaga(Long id, ParkingSpot parkingSpotDetails) {
	    ParkingSpot existingVaga = parkingSpotRepository.findById(id)
	        .orElseThrow(() -> new RuntimeException("Vaga não encontrada"));

	    existingVaga.setTipo(parkingSpotDetails.getTipo());
	    existingVaga.setStatus(parkingSpotDetails.getStatus());
	    return parkingSpotRepository.save(existingVaga);
	}
	
	public List<ParkingSpot> findAll() {
	    List<ParkingSpot> parkingSpots = parkingSpotRepository.findAll();

	    // Se não houver nenhuma vaga cadastrada, lança uma exceção
	    if (parkingSpots.isEmpty()) {

	        throw new ObjectNotFoundException("Nenhuma vaga cadastrada no sistema.");
	    }

	    return parkingSpots;
	}
	
	// Método para gerar um número único para a vaga com base no tipo
		private String gerarNumeroVaga(VacancyType tipo) {
			String ultimoNumero = parkingSpotRepository.findUltimoNumeroPorTipo(tipo);
			int proximoNumero = (ultimoNumero != null) ? Integer.parseInt(ultimoNumero.substring(1)) + 1 : 1;
			return String.format("%s%02d", tipo.getPrefixo(), proximoNumero);
		}
}