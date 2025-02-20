package com.estacionamento.estacionamento.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.estacionamento.estacionamento.models.ParkingSpot;
import com.estacionamento.estacionamento.models.VacancyStatus;
import com.estacionamento.estacionamento.models.VacancyType;
import com.estacionamento.estacionamento.repository.ParkingSpotRepository;

@Service
public class ParkingSpotService {

	 @Autowired
	    private ParkingSpotRepository parkingSpotRepository;

	    // Buscar todas as vagas
	    public List<ParkingSpot> findAll() {
	        return parkingSpotRepository.findAll();
	    }

	    // Buscar vaga por ID
	    public Optional<ParkingSpot> findById(Long id) {
	        return parkingSpotRepository.findById(id);
	    }

	    // Criar nova vaga
	    public ParkingSpot criarVaga(VacancyType tipo, VacancyStatus status) {
	        String numero = gerarNumeroVaga(tipo);
	        ParkingSpot vaga = new ParkingSpot(numero, tipo, status);
	        return parkingSpotRepository.save(vaga);
	    }
	    // Método para gerar um número único para a vaga com base no tipo
	    private String gerarNumeroVaga(VacancyType tipo) {
	        String ultimoNumero = parkingSpotRepository.findUltimoNumeroPorTipo(tipo);
	        int proximoNumero = (ultimoNumero != null) ? Integer.parseInt(ultimoNumero.substring(1)) + 1 : 1;
	        return String.format("%s%02d", tipo.getPrefixo(), proximoNumero);
	    }

	    // Deletar vaga por ID (verificando disponibilidade)
	    public void deleteById(Long id) {
	        Optional<ParkingSpot> vagaOpt = parkingSpotRepository.findById(id);
	        
	        if (vagaOpt.isEmpty()) {
	            throw new IllegalArgumentException("Vaga não encontrada.");
	        }

	        ParkingSpot vaga = vagaOpt.get();

	        // Verifica se a vaga está disponível antes de tentar excluir
	        if (!vaga.isDisponivel()) {
	            throw new IllegalStateException("Não é possível excluir uma vaga reservada ou ocupada.");
	        }

	        parkingSpotRepository.delete(vaga);
	    }

	    // Atualizar vaga
	    public ParkingSpot atualizarVaga(Long id, ParkingSpot novaVaga) {
	        ParkingSpot vagaExistente = parkingSpotRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Vaga não encontrada!"));

	        // Verifica se a vaga está disponível para atualização
	        if (!vagaExistente.isDisponivel()) {
	            throw new RuntimeException("A vaga não pode ser atualizada, pois não está disponível.");
	        }

	        // Se o tipo da vaga mudou, geramos um novo número
	        if (!vagaExistente.getTipo().equals(novaVaga.getTipo())) {
	            vagaExistente.setTipo(novaVaga.getTipo());
	            vagaExistente.setNumero(gerarNumeroVaga(novaVaga.getTipo())); // Atualiza o número da vaga
	        }

	        // Não é mais necessário atualizar o valor por hora, pois é obtido diretamente do VacancyType
	        // vagaExistente.setValorPorHora(BigDecimal.valueOf(novaVaga.getTipo().getValorPorHora()));

	        return parkingSpotRepository.save(vagaExistente);
	    }
}
