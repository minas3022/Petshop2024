package com.petshop.petshop.controller;

import com.petshop.petshop.model.Cliente;
import com.petshop.petshop.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class ClienteControllerTest {

    @InjectMocks
    private ClienteController clienteController;

    @Mock
    private ClienteService clienteService;

    @Mock
    private Model model;

    @Mock
    private ModelMap modelMap;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MockMvc mockMvc1;

    private Cliente cliente;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Teste Cliente");
        // Preencha outros campos conforme necessário
    }

    @BeforeEach
    public void setup1() {
        // Initialize Mockito annotations
        MockitoAnnotations.openMocks(this);
        // Initialize MockMvc with the controller
        mockMvc1 = MockMvcBuilders.standaloneSetup(clienteController).build();
    }

    @Test
    @WithMockUser
    public void testCadastrar() throws Exception {
        mockMvc1.perform(get("/clientes/cadastrar"))
                .andExpect(status().isOk())
                .andExpect(view().name("cliente/cadastro"))
                .andExpect(model().attributeExists("cliente"));
    }

    @Test
    public void testListar() {
        when(clienteService.getAll()).thenReturn(List.of(cliente));
        String viewName = clienteController.listar(modelMap);
        assertThat(viewName).isEqualTo("cliente/lista");
        verify(modelMap).addAttribute("clientes", clienteService.getAll());
    }

    @Test
    public void testSalvarClienteComErros() {
        when(bindingResult.hasErrors()).thenReturn(true);
        String viewName = clienteController.salvar(cliente, bindingResult, redirectAttributes);
        assertThat(viewName).isEqualTo("cliente/cadastro");
    }

    @Test
    public void testSalvarClienteSemErros() {
        when(bindingResult.hasErrors()).thenReturn(false);
        String viewName = clienteController.salvar(cliente, bindingResult, redirectAttributes);
        verify(clienteService).create(cliente);
        verify(redirectAttributes).addFlashAttribute("success", "Cliente inserido com sucesso.");
        assertThat(viewName).isEqualTo("redirect:/clientes/listar");
    }

    @Test
    public void testExcluirCliente() {
        doNothing().when(clienteService).delete(1L);
        String viewName = clienteController.excluir(1L, redirectAttributes);
        verify(redirectAttributes).addFlashAttribute("success", "Cliente excluído com sucesso.");
        assertThat(viewName).isEqualTo("redirect:/clientes/listar");
    }

    @Test
    public void testExcluirClienteComViolacao() {
        doThrow(new DataIntegrityViolationException("violação")).when(clienteService).delete(1L);
        String viewName = clienteController.excluir(1L, redirectAttributes);
        assertThat(viewName).isEqualTo("redirect:/clientes/listar?error=violacao-chave-estrangeira");
    }

    @Test
    public void testPreEditar() {
        when(clienteService.getById(1L)).thenReturn(Optional.of(cliente));
        String viewName = clienteController.preEditar(1L, modelMap);
        verify(modelMap).addAttribute("cliente", cliente);
        assertThat(viewName).isEqualTo("cliente/editar");
    }

    @Test
    public void testEditarClienteComErros() {
        when(bindingResult.hasErrors()).thenReturn(true);
        String viewName = clienteController.editar(cliente, bindingResult, redirectAttributes);
        assertThat(viewName).isEqualTo("cliente/cadastro");
    }

    @Test
    public void testEditarClienteSemErros() {
        when(bindingResult.hasErrors()).thenReturn(false);
        String viewName = clienteController.editar(cliente, bindingResult, redirectAttributes);
        verify(clienteService).update(cliente.getId(), cliente);
        verify(redirectAttributes).addFlashAttribute("success", "Cliente atualizado com sucesso.");
        assertThat(viewName).isEqualTo("redirect:/clientes/listar");
    }
}
