package com.petshop.petshop.controller;

import com.petshop.petshop.model.Pet;
import com.petshop.petshop.model.Servico;
import com.petshop.petshop.service.PetService;
import com.petshop.petshop.service.ServicoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



public class PetControllerTes {

    @InjectMocks
    private PetController petController;

    @Mock
    private Pet pet;

    @Mock
    private PetService petService;

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
        mockMvc = MockMvcBuilders.standaloneSetup(petController).build();
    }
    @BeforeEach
    public void setup1() {
        // Initialize Mockito annotations
        MockitoAnnotations.openMocks(this);
        // Initialize MockMvc with the controller
        mockMvc1 = MockMvcBuilders.standaloneSetup(petController).build();
    }

    @Test
    public void testCadastrar() {
        String viewName = petController.cadastrar(model);
        verify(model, times(1)).addAttribute(eq("pet"), any(Pet.class));
        assertEquals("pet/cadastro", viewName);
    }

    @Test
    public void testListar() {
        when(petService.getAll()).thenReturn(List.of(pet));
        String viewName = petController.listar(modelMap);
        assertThat(viewName).isEqualTo("pet/lista");
        verify(modelMap).addAttribute("pet", petService.getAll());
    }

    @Test
    public void testSalvarPetComErros() {
        when(bindingResult.hasErrors()).thenReturn(true);
        String viewName = petController.salvar(pet, bindingResult, redirectAttributes);
        assertThat(viewName).isEqualTo("pet/cadastro");
    }

    @Test
    public void testSalvarPetSemErros() {
        when(bindingResult.hasErrors()).thenReturn(false);
        String viewName = petController.salvar(pet, bindingResult, redirectAttributes);

        verify(petService).create(pet);
        verify(redirectAttributes).addFlashAttribute("success", "Pet inserido com sucesso.");
        assertThat(viewName).isEqualTo("redirect:/pets/listar");
    }

    @Test
    public void testExcluirPet() {
        doNothing().when(petService).delete(1L);
        String viewName = petController.excluir(1L, redirectAttributes);
        verify(redirectAttributes).addFlashAttribute("success", "Pet excluído com sucesso.");
        assertThat(viewName).isEqualTo("redirect:/pets/listar");
    }

    @Test
    public void testPreEditar() {
        when(petService.getById(1L)).thenReturn(Optional.of(pet));
        String viewName = petController.preEditar(1L, modelMap);
        verify(modelMap).addAttribute("pet", pet);
        assertThat(viewName).isEqualTo("pet/editar");
    }

}
