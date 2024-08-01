package com.petshop.petshop.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.petshop.petshop.model.Agendamento;
import com.petshop.petshop.model.Cliente;
import com.petshop.petshop.model.Pet;
import com.petshop.petshop.model.Servico;
import com.petshop.petshop.service.AgendamentoService;
import com.petshop.petshop.service.ClienteService;
import com.petshop.petshop.service.PetService;
import com.petshop.petshop.service.ServicoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class AgendamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private AgendamentoService service;

    @Mock
    private ClienteService clienteService;

    @Mock
    private PetService petService;

    @Mock
    private ServicoService servicoService;

    @InjectMocks
    private AgendamentoController agendamentoController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(agendamentoController).build();
    }

    @Test
    @WithMockUser
    public void testCadastrar() throws Exception {
        mockMvc.perform(get("/agendamentos/cadastrar"))
                .andExpect(status().isOk())
                .andExpect(view().name("agendamento/cadastro"))
                .andExpect(model().attributeExists("agendamento"));
    }

    @Test
    public void testListar() throws Exception {
        when(service.getAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/agendamentos/listar"))
                .andExpect(status().isOk())
                .andExpect(view().name("agendamento/lista"))
                .andExpect(model().attributeExists("agendamentos"));
    }

    @Test
    @WithMockUser
    public void testSalvar() throws Exception {
        Agendamento agendamento = new Agendamento();
        agendamento.setId(1L);
        agendamento.setCliente(new Cliente()); // Substitua por um cliente válido
        agendamento.setServico(new Servico()); // Substitua por um serviço válido
        agendamento.setPet(new Pet()); // Substitua por um pet válido
        agendamento.setDataHora(LocalDateTime.now()); // Ajuste conforme necessário

        when(service.create(any(Agendamento.class))).thenReturn(agendamento); // Mock do serviço

        mockMvc.perform(post("/agendamentos/salvar")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("cliente.id", "1") // Use o ID ou valores corretos
                        .param("servico.id", "1") // Use o ID ou valores corretos
                        .param("pet.id", "1") // Use o ID ou valores corretos
                        .param("dataHora", "2024-07-17T10:00:00")) // Ajuste para o formato correto
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/agendamentos/listar"));

        verify(service, times(1)).create(any(Agendamento.class));
    }

}