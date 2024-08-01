package com.petshop.petshop.controller;

import com.petshop.petshop.model.Cliente;
import com.petshop.petshop.model.Servico;
import com.petshop.petshop.service.ServicoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ServicoControllerTest {

    @InjectMocks
    private ServicoController servicoController;

    @Mock
    private Servico servico;

    @Mock
    private ServicoService servicoService;

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

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        servico = new Servico();
        servico.setId(1L);
        servico.setNome("Teste Servico");
        // Preencha outros campos conforme necessário
    }
    @BeforeEach
    public void setup1() {
        // Initialize Mockito annotations
        MockitoAnnotations.openMocks(this);
        // Initialize MockMvc with the controller
        mockMvc1 = MockMvcBuilders.standaloneSetup(servicoController).build();
    }

    @Test
    @WithMockUser
    public void testCadastrar() throws Exception {
        mockMvc1.perform(get("/servicos/cadastrar"))
                .andExpect(status().isOk())
                .andExpect(view().name("servico/cadastro"))
                .andExpect(model().attributeExists("servico"));
    }

    @Test
    public void testListar() {
        when(servicoService.getAll()).thenReturn(List.of(servico));
        String viewName = servicoController.listar(modelMap);
        assertThat(viewName).isEqualTo("servico/lista");
        verify(modelMap).addAttribute("servico", servicoService.getAll());
    }

    @Test
    public void testSalvarServicoComErros() {
        when(bindingResult.hasErrors()).thenReturn(true);
        String viewName = servicoController.salvar(servico, bindingResult, redirectAttributes);
        assertThat(viewName).isEqualTo("servico/cadastro");
    }

    @Test
    public void testSalvarServicoSemErros() {
        when(bindingResult.hasErrors()).thenReturn(false);
        String viewName = servicoController.salvar(servico, bindingResult, redirectAttributes);

        verify(servicoService).create(servico);
        verify(redirectAttributes).addFlashAttribute("success", "Serviço inserido com sucesso.");
        assertThat(viewName).isEqualTo("redirect:/servicos/listar");
    }

    @Test
    public void testExcluirServico() {
        doNothing().when(servicoService).delete(1L);
        String viewName = servicoController.excluir(1L, redirectAttributes);
        verify(redirectAttributes).addFlashAttribute("success", "Serviço excluído com sucesso.");
        assertThat(viewName).isEqualTo("redirect:/servicos/listar");
    }

    @Test
    public void testPreEditar() {
        when(servicoService.getById(1L)).thenReturn(Optional.of(servico));
        String viewName = servicoController.preEditar(1L, modelMap);
        verify(modelMap).addAttribute("servico", servico);
        assertThat(viewName).isEqualTo("servico/editar");
    }

}
