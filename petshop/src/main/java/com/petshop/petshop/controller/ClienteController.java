package com.petshop.petshop.controller;


import com.petshop.petshop.model.Cliente;
import com.petshop.petshop.service.ClienteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

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
@RequestMapping("/clientes")
@Tag(name = "Cliente", description = "Operações relacionadas a clientes")
public class ClienteController {

    @Autowired
    private ClienteService service;

    @GetMapping("/cadastrar")
    @Operation(summary = "Mostrar formulário de cadastro de cliente")
    public String cadastrar(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "cliente/cadastro";
    }

    @GetMapping("/listar")
    @Operation(summary = "Listar todos os clientes")
    @ApiResponse(responseCode = "200", description = "Clientes listados com sucesso")
    public String listar(ModelMap model) {
        List<Cliente> clientes = service.getAll();
        model.addAttribute("clientes", clientes);
        return "cliente/lista";
    }

    @PostMapping("/salvar")
    @Operation(summary = "Salvar um novo cliente")
    @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro de validação de dados")
    public String salvar(@Valid @ModelAttribute Cliente cliente, BindingResult result, RedirectAttributes attr) {
        if (result.hasErrors()) {
            return "cliente/cadastro";
        }
        service.create(cliente);
        attr.addFlashAttribute("success", "Cliente inserido com sucesso.");
        return "redirect:/clientes/listar";
    }

    @GetMapping("/excluir/{id}")
    @Operation(summary = "Excluir um cliente")
    @ApiResponse(responseCode = "200", description = "Cliente excluído com sucesso")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    @ApiResponse(responseCode = "409", description = "Erro de violação de integridade referencial")
    public String excluir(@PathVariable("id") Long id, RedirectAttributes attr) {
        try {
            service.delete(id);
            attr.addFlashAttribute("success", "Cliente excluído com sucesso.");
            return "redirect:/clientes/listar";
        } catch (DataIntegrityViolationException e) {
            return "redirect:/clientes/listar?error=violacao-chave-estrangeira";
        }
    }

    @GetMapping("/editar/{id}")
    @Operation(summary = "Mostrar formulário de edição de cliente")
    @ApiResponse(responseCode = "200", description = "Formulário de edição exibido com sucesso")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    public String preEditar(@PathVariable("id") Long id, ModelMap model) {
        Cliente cliente = service.getById(id).orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado: " + id));
        model.addAttribute("cliente", cliente);
        return "cliente/editar";
    }

    @PostMapping("/editar")
    @Operation(summary = "Atualizar um cliente existente")
    @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro de validação de dados")
    public String editar(@Valid @ModelAttribute("cliente") Cliente cliente, BindingResult result, RedirectAttributes attr) {
        if (result.hasErrors()) {
            return "cliente/cadastro";
        }
        attr.addFlashAttribute("success", "Cliente atualizado com sucesso.");
        service.update(cliente.getId(), cliente);
        return "redirect:/clientes/listar";
    }


       //pesquisar clientes(carlos)
       @GetMapping
       public String getAllClientes(Model model, @RequestParam(value = "search", required = false) String search) {
           List<Cliente> clientes;
                   if (search != null && !search.isEmpty()) {
               clientes = service.searchByNome(search);
           } else {
               clientes = service.findAll();
           }
           model.addAttribute("clientes", clientes);
           model.addAttribute("search", "");
           return "cliente/lista";
       }
}

