package com.petshop.petshop.controller;

import com.petshop.petshop.model.Cliente;
import com.petshop.petshop.model.Pet;
import com.petshop.petshop.service.ClienteService;
import com.petshop.petshop.service.PetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Controller
@RequestMapping("/pets")
@Tag(name = "Pet", description = "Cadastro e gerenciamento de Pets")
public class PetController {

    @Autowired
    private PetService service;

    @Autowired
    private ClienteService clienteService;

    @Operation(summary = "Exibe o formulário para criar um novo pet")
    @GetMapping("/cadastrar")
    @ApiResponse(responseCode = "200", description = "Formulário exibido com sucesso")
    public String cadastrar(Model model) {
        model.addAttribute("pet", new Pet());
        return "pet/cadastro";
    }

    @Operation(summary = "Lista todos os pets")
    @GetMapping("/listar")
    @ApiResponse(responseCode = "200", description = "Lista de pets exibida com sucesso")
    public String listar(ModelMap model) {
        model.addAttribute("pet", service.getAll());
        return "pet/lista";
    }

    @Operation(summary = "Salva um novo pet")
    @PostMapping("/salvar")
    @ApiResponse(responseCode = "201", description = "Pet criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro de validação de dados")
    public String salvar(@Valid @ModelAttribute Pet pet, BindingResult result, RedirectAttributes attr) {
        if (result.hasErrors()) {
            return "pet/cadastro";
        }
        service.create(pet);
        attr.addFlashAttribute("success", "Pet inserido com sucesso.");
        return "redirect:/pets/listar";
    }

    @Operation(summary = "Exclui um pet pelo seu ID")
    @GetMapping("/excluir/{id}")
    @ApiResponse(responseCode = "200", description = "Pet excluído com sucesso")
    @ApiResponse(responseCode = "404", description = "Pet não encontrado")
    public String excluir(@PathVariable("id") Long id, RedirectAttributes attr) {
        service.delete(id);
        attr.addFlashAttribute("success", "Pet excluído com sucesso.");
        return "redirect:/pets/listar";
    }

    @Operation(summary = "Exibe o formulário para editar um pet existente")
    @GetMapping("/editar/{id}")
    @ApiResponse(responseCode = "200", description = "Formulário de edição exibido com sucesso")
    @ApiResponse(responseCode = "404", description = "Pet não encontrado")
    public String preEditar(@PathVariable("id") Long id, ModelMap model) {
        Pet pet = service.getById(id).orElseThrow(() -> new IllegalArgumentException("Pet não encontrado: " + id));
        model.addAttribute("pet", pet);
        return "pet/editar"; // Nome da página Thymeleaf para edição
    }

    @Operation(summary = "Atualiza um pet existente")
    @PostMapping("/editar")
    @ApiResponse(responseCode = "200", description = "Pet atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro de validação de dados")
    public String editar(@Valid @ModelAttribute("pet") Pet pet, BindingResult result, RedirectAttributes attr) {
        if (result.hasErrors()) {
            return "pet/cadastro";
        }
        service.update(pet.getId(), pet);
        attr.addFlashAttribute("success", "Pet atualizado com sucesso.");
        return "redirect:/pets/listar"; // Redireciona para listagem após editar
    }

    @ModelAttribute("cliente")
    public List<Cliente> getClientes() {
        return clienteService.getAll();
    }
}