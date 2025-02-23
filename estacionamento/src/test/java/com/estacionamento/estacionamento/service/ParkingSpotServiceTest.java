package com.estacionamento.estacionamento.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.estacionamento.estacionamento.exceptions.ObjectNotFoundException;
import com.estacionamento.estacionamento.exceptions.ParkingSpotNotAvailableException;
import com.estacionamento.estacionamento.exceptions.ParkingSpotNotFoundException;
import com.estacionamento.estacionamento.models.ParkingSpot;
import com.estacionamento.estacionamento.models.VacancyStatus;
import com.estacionamento.estacionamento.models.VacancyType;
import com.estacionamento.estacionamento.repository.ParkingSpotRepository;
import com.estacionamento.estacionamento.services.ParkingSpotService;

@ExtendWith(MockitoExtension.class)
class ParkingSpotServiceTest {

    @Mock
    private ParkingSpotRepository parkingSpotRepository;

    @InjectMocks
    private ParkingSpotService parkingSpotService;

    private ParkingSpot parkingSpot;

    @BeforeEach
    void setUp() {
        // Configuração comum para os testes
        parkingSpot = new ParkingSpot();
        parkingSpot.setId(1L);
        parkingSpot.setNumero("C01");
        parkingSpot.setTipo(VacancyType.COMUM);
        parkingSpot.setStatus(VacancyStatus.DISPONIVEL);
    }

    // Teste para criar uma vaga
    @Test
    void criarVaga_ShouldReturnCreatedParkingSpot() {
        // Configuração do mock
        when(parkingSpotRepository.findUltimoNumeroPorTipo(VacancyType.COMUM)).thenReturn("C01");
        when(parkingSpotRepository.save(any(ParkingSpot.class))).thenAnswer(invocation -> {
            ParkingSpot vagaSalva = invocation.getArgument(0);
            vagaSalva.setId(1L); // Simula a geração do ID no banco de dados
            return vagaSalva;
        });

        // Execução do método
        ParkingSpot createdVaga = parkingSpotService.criarVaga(VacancyType.COMUM, VacancyStatus.DISPONIVEL);

        // Verificações
        assertNotNull(createdVaga);
        assertEquals("C02", createdVaga.getNumero()); // Próximo número gerado
        assertEquals(VacancyType.COMUM, createdVaga.getTipo());
        assertEquals(VacancyStatus.DISPONIVEL, createdVaga.getStatus());
        verify(parkingSpotRepository, times(1)).save(any(ParkingSpot.class));
    }

    // Teste para criar uma vaga com tipo ou status nulo
    @Test
    void criarVaga_WhenTypeOrStatusIsNull_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            parkingSpotService.criarVaga(null, VacancyStatus.DISPONIVEL);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            parkingSpotService.criarVaga(VacancyType.COMUM, null);
        });
    }

    // Teste para buscar uma vaga por ID
    @Test
    void findById_ShouldReturnParkingSpot() {
        // Configuração do mock
        when(parkingSpotRepository.findById(1L)).thenReturn(Optional.of(parkingSpot));

        // Execução do método
        ParkingSpot foundVaga = parkingSpotService.findById(1L);

        // Verificações
        assertNotNull(foundVaga);
        assertEquals(1L, foundVaga.getId());
    }

    // Teste para buscar uma vaga por ID inexistente
    @Test
    void findById_WhenParkingSpotNotFound_ShouldThrowException() {
        // Configuração do mock
        when(parkingSpotRepository.findById(1L)).thenReturn(Optional.empty());

        // Execução e verificação da exceção
        assertThrows(ParkingSpotNotFoundException.class, () -> {
            parkingSpotService.findById(1L);
        });
    }

    // Teste para deletar uma vaga por ID
    @Test
    void deleteById_ShouldDeleteParkingSpot() throws ParkingSpotNotAvailableException {
        // Configuração do mock
        when(parkingSpotRepository.existsById(1L)).thenReturn(true);

        // Execução do método
        parkingSpotService.deleteById(1L);

        // Verificações
        verify(parkingSpotRepository, times(1)).deleteById(1L);
    }

    // Teste para deletar uma vaga por ID inexistente
    @Test
    void deleteById_WhenParkingSpotNotFound_ShouldThrowException() {
        // Configuração do mock
        when(parkingSpotRepository.existsById(1L)).thenReturn(false);

        // Execução e verificação da exceção
        assertThrows(ParkingSpotNotFoundException.class, () -> {
            parkingSpotService.deleteById(1L);
        });
    }

    // Teste para atualizar uma vaga
    @Test
    void atualizarVaga_ShouldReturnUpdatedParkingSpot() {
        // Configuração do mock
        when(parkingSpotRepository.findById(1L)).thenReturn(Optional.of(parkingSpot));
        when(parkingSpotRepository.save(any(ParkingSpot.class))).thenReturn(parkingSpot);

        // Dados para atualização
        ParkingSpot updatedDetails = new ParkingSpot();
        updatedDetails.setTipo(VacancyType.VIP);
        updatedDetails.setStatus(VacancyStatus.RESERVADA);

        // Execução do método
        ParkingSpot updatedVaga = parkingSpotService.atualizarVaga(1L, updatedDetails);

        // Verificações
        assertNotNull(updatedVaga);
        assertEquals(VacancyType.VIP, updatedVaga.getTipo());
        assertEquals(VacancyStatus.RESERVADA, updatedVaga.getStatus());
        verify(parkingSpotRepository, times(1)).save(parkingSpot);
    }

    // Teste para atualizar uma vaga inexistente
    @Test
    void atualizarVaga_WhenParkingSpotNotFound_ShouldThrowException() {
        // Configuração do mock
        when(parkingSpotRepository.findById(1L)).thenReturn(Optional.empty());

        // Execução e verificação da exceção
        assertThrows(RuntimeException.class, () -> {
            parkingSpotService.atualizarVaga(1L, new ParkingSpot());
        });
    }

    // Teste para buscar todas as vagas
    @Test
    void findAll_ShouldReturnListOfParkingSpots() {
        // Configuração do mock
        when(parkingSpotRepository.findAll()).thenReturn(Arrays.asList(parkingSpot));

        // Execução do método
        List<ParkingSpot> parkingSpots = parkingSpotService.findAll();

        // Verificações
        assertFalse(parkingSpots.isEmpty());
        assertEquals(1, parkingSpots.size());
    }

    // Teste para buscar todas as vagas quando não há vagas cadastradas
    @Test
    void findAll_WhenNoParkingSpotsFound_ShouldThrowException() {
        // Configuração do mock
        when(parkingSpotRepository.findAll()).thenReturn(Arrays.asList());

        // Execução e verificação da exceção
        assertThrows(ObjectNotFoundException.class, () -> {
            parkingSpotService.findAll();
        });
    }
}