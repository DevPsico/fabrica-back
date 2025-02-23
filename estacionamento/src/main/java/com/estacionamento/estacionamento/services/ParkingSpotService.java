package com.estacionamento.estacionamento.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.estacionamento.estacionamento.exceptions.ObjectNotFoundException;
import com.estacionamento.estacionamento.exceptions.ParkingSpotNotAvailableException;
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
		return parkingSpot.orElseThrow(
				() -> new ParkingSpotNotFoundException("Vaga de estacionamento não encontrada com o ID: " + id));
	}

	public void deleteById(Long id) throws ParkingSpotNotAvailableException {
	    // Verifica se a vaga existe
	    ParkingSpot parkingSpot = parkingSpotRepository.findById(id)
	        .orElseThrow(() -> new ParkingSpotNotFoundException("Vaga não encontrada com o ID: " + id));

	 // Verifica se a vaga está disponível
	    if (parkingSpot.getStatus() != VacancyStatus.DISPONIVEL) {
	        throw new ParkingSpotNotAvailableException("Não é possível deletar a vaga, pois ela não está disponível.");
	    }

	    // Se a vaga não estiver ocupada, prossegue com a exclusão
	    parkingSpotRepository.deleteById(id);
	}

	public ParkingSpot atualizarVaga(Long id, ParkingSpot parkingSpotDetails) {

	    // Verifica se a vaga existe, caso contrário, lança exceção
	    ParkingSpot existingVaga = parkingSpotRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Vaga não encontrada"));

	    // Verifica se a vaga está disponível antes de permitir a atualização
	    if (existingVaga.getStatus() != VacancyStatus.DISPONIVEL) {
	        throw new ParkingSpotNotFoundException("A vaga não está disponível e não pode ser atualizada.");
	    }

	    // Atualiza os dados da vaga com as informações fornecidas
	    existingVaga.setTipo(parkingSpotDetails.getTipo());
	    existingVaga.setStatus(parkingSpotDetails.getStatus());

	    // Gera um novo número de vaga baseado no tipo
	    String novoNumero = gerarNumeroVaga(parkingSpotDetails.getTipo());
	    existingVaga.setNumero(novoNumero);

	    // Salva e retorna a vaga atualizada
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