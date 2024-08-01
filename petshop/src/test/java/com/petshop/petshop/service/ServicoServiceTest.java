package com.petshop.petshop.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.petshop.petshop.model.Servico;
import com.petshop.petshop.repository.ServicoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ServicoServiceTest {

    @InjectMocks
    private ServicoService servicoService;

    @Mock
    private ServicoRepository servicoRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAll() {
        // Arrange
        Servico servico1 = new Servico();
        Servico servico2 = new Servico();
        when(servicoRepository.findAll()).thenReturn(Arrays.asList(servico1, servico2));

        // Act
        List<Servico> result = servicoService.getAll();

        // Assert
        assertEquals(2, result.size());
        verify(servicoRepository, times(1)).findAll();
    }

    @Test
    public void testGetById() {
        // Arrange
        Long id = 1L;
        Servico servico = new Servico();
        when(servicoRepository.findById(id)).thenReturn(Optional.of(servico));

        // Act
        Optional<Servico> result = servicoService.getById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(servico, result.get());
        verify(servicoRepository, times(1)).findById(id);
    }

    @Test
    public void testCreate() {
        // Arrange
        Servico servico = new Servico();
        when(servicoRepository.save(servico)).thenReturn(servico);

        // Act
        Servico result = servicoService.create(servico);

        // Assert
        assertEquals(servico, result);
        verify(servicoRepository, times(1)).save(servico);
    }

    @Test
    public void testUpdate_WhenExists() {
        // Arrange
        Long id = 1L;
        Servico servico = new Servico();
        when(servicoRepository.existsById(id)).thenReturn(true);
        when(servicoRepository.save(servico)).thenReturn(servico);

        // Act
        Optional<Servico> result = servicoService.update(id, servico);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(servico, result.get());
        verify(servicoRepository, times(1)).existsById(id);
        verify(servicoRepository, times(1)).save(servico);
    }

    @Test
    public void testUpdate_WhenNotExists() {
        // Arrange
        Long id = 1L;
        Servico servico = new Servico();
        when(servicoRepository.existsById(id)).thenReturn(false);

        // Act
        Optional<Servico> result = servicoService.update(id, servico);

        // Assert
        assertFalse(result.isPresent());
        verify(servicoRepository, times(1)).existsById(id);
        verify(servicoRepository, never()).save(any());
    }

    @Test
    public void testDelete() {
        // Arrange
        Long id = 1L;

        // Act
        servicoService.delete(id);

        // Assert
        verify(servicoRepository, times(1)).deleteById(id);
    }
}
