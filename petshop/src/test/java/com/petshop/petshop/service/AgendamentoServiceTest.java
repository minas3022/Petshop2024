package com.petshop.petshop.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.petshop.petshop.model.Agendamento;
import com.petshop.petshop.repository.AgendamentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class AgendamentoServiceTest {

    @InjectMocks
    private AgendamentoService agendamentoService;

    @Mock
    private AgendamentoRepository agendamentoRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAllWithDetails() {
        Agendamento agendamento1 = new Agendamento(); // Preencha com dados necessários
        Agendamento agendamento2 = new Agendamento(); // Preencha com dados necessários

        when(agendamentoRepository.findAll()).thenReturn(Arrays.asList(agendamento1, agendamento2));

        List<Agendamento> agendamentos = agendamentoService.getAll();

        assertEquals(2, agendamentos.size());
        verify(agendamentoRepository, times(1)).findAll();
    }

    @Test
    public void testGetById() {
        Long id = 1L;
        Agendamento agendamento = new Agendamento(); // Preencha com dados necessários
        when(agendamentoRepository.findById(id)).thenReturn(Optional.of(agendamento));

        Optional<Agendamento> result = agendamentoService.getById(id);

        assertTrue(result.isPresent());
        assertEquals(agendamento, result.get());
        verify(agendamentoRepository, times(1)).findById(id);
    }

    @Test
    public void testCreate() {
        Agendamento agendamento = new Agendamento(); // Preencha com dados necessários
        when(agendamentoRepository.save(agendamento)).thenReturn(agendamento);

        Agendamento createdAgendamento = agendamentoService.create(agendamento);

        assertEquals(agendamento, createdAgendamento);
        verify(agendamentoRepository, times(1)).save(agendamento);
    }

    @Test
    public void testUpdate_WhenExists() {
        Long id = 1L;
        Agendamento agendamento = new Agendamento(); // Preencha com dados necessários
        when(agendamentoRepository.existsById(id)).thenReturn(true);
        when(agendamentoRepository.save(agendamento)).thenReturn(agendamento);

        Optional<Agendamento> result = agendamentoService.update(id, agendamento);

        assertTrue(result.isPresent());
        assertEquals(agendamento, result.get());
        verify(agendamentoRepository, times(1)).existsById(id);
        verify(agendamentoRepository, times(1)).save(agendamento);
    }

    @Test
    public void testUpdate_WhenNotExists() {
        Long id = 1L;
        Agendamento agendamento = new Agendamento(); // Preencha com dados necessários
        when(agendamentoRepository.existsById(id)).thenReturn(false);

        Optional<Agendamento> result = agendamentoService.update(id, agendamento);

        assertFalse(result.isPresent());
        verify(agendamentoRepository, times(1)).existsById(id);
        verify(agendamentoRepository, never()).save(any());
    }

    @Test
    public void testDelete() {
        Long id = 1L;

        agendamentoService.delete(id);

        verify(agendamentoRepository, times(1)).deleteById(id);
    }
}
