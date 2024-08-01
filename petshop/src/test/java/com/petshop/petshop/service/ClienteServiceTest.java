package com.petshop.petshop.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.petshop.petshop.model.Cliente;
import com.petshop.petshop.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ClienteServiceTest {

    @InjectMocks
    private ClienteService clienteService;

    @Mock
    private ClienteRepository clienteRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAll() {
        Cliente cliente1 = new Cliente();
        Cliente cliente2 = new Cliente();
        cliente1.setId(1l);
        cliente1.setNome("Valdir");
        cliente1.setTelefone("999999");
        cliente1.setEmail("valdir@gmail.com");

        cliente2.setId(2l);
        cliente2.setNome("Valdir");
        cliente2.setTelefone("999999");
        cliente2.setEmail("valdir@gmail.com");
        when(clienteRepository.findAll()).thenReturn(Arrays.asList(cliente1, cliente2));

        List<Cliente> clientes = clienteService.getAll();

        assertEquals(2, clientes.size());
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    public void testGetById() {
        Cliente cliente1 = new Cliente();
        cliente1.setId(1l);
        cliente1.setNome("Valdir");
        cliente1.setTelefone("999999");
        cliente1.setEmail("valdir@gmail.com");
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente1));

        Optional<Cliente> foundCliente = clienteService.getById(1L);

        assertTrue(foundCliente.isPresent());
        assertEquals(cliente1.getNome(), foundCliente.get().getNome());
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    public void testCreate() {
        // Criando um cliente sem ID (simulando um novo cliente)
        Cliente cliente = new Cliente();
        cliente.setId(1l);
        cliente.setNome("Valdir");
        cliente.setTelefone("999999");
        cliente.setEmail("valdir@gmail.com");
        // Simulando o comportamento do repositório
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(invocation -> {
            Cliente savedCliente = invocation.getArgument(0);
            savedCliente.setId(1L); // Simulando a atribuição de um ID ao cliente salvo
            return savedCliente;
        });

        // Chamando o método de criação
        Cliente createdCliente = clienteService.create(cliente);

        // Verificando os resultados
        assertNotNull(createdCliente.getId());
        assertEquals(cliente.getNome(), createdCliente.getNome());
        verify(clienteRepository, times(1)).save(cliente);
    }


    @Test
    public void testUpdate() {
        Cliente cliente = new Cliente();

        when(clienteRepository.existsById(1L)).thenReturn(true);
        when(clienteRepository.save(cliente)).thenReturn(new Cliente());

        Optional<Cliente> updatedCliente = clienteService.update(1L, cliente);

        assertTrue(updatedCliente.isPresent());
        assertEquals(cliente.getNome(), updatedCliente.get().getNome());
        verify(clienteRepository, times(1)).existsById(1L);
        verify(clienteRepository, times(1)).save(cliente);
    }

    @Test
    public void testUpdateNotFound() {
        Cliente cliente = new Cliente();
        cliente.setId(1l);
        cliente.setNome("Valdir");
        cliente.setTelefone("999999");
        cliente.setEmail("valdir@gmail.com");
        when(clienteRepository.existsById(2L)).thenReturn(false);

        Optional<Cliente> updatedCliente = clienteService.update(2L, cliente);

        assertFalse(updatedCliente.isPresent());
        verify(clienteRepository, times(1)).existsById(2L);
    }

    @Test
    public void testDelete() {
        doNothing().when(clienteRepository).deleteById(1L);

        clienteService.delete(1L);

        verify(clienteRepository, times(1)).deleteById(1L);
    }
}
