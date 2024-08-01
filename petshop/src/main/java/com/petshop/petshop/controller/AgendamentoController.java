package com.petshop.petshop.controller;


import com.petshop.petshop.model.Agendamento;
import com.petshop.petshop.model.Cliente;
import com.petshop.petshop.model.Pet;
import com.petshop.petshop.model.Servico;
import com.petshop.petshop.service.AgendamentoService;
import com.petshop.petshop.service.ClienteService;
import com.petshop.petshop.service.PetService;
import com.petshop.petshop.service.ServicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/agendamentos")
@Tag(name = "Agendamento", description = "Cadastro de Agendamento de Serviços")
public class AgendamentoController {

    @Autowired
    private AgendamentoService service;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private PetService petService;

    @Autowired
    private ServicoService servicoService;

    @Operation(summary = "Exibe o formulário para criar um novo agendamento")
    @GetMapping("/cadastrar")
    @ApiResponse(responseCode = "200", description = "Formulário exibido com sucesso")
    public String cadastrar(Model model) {
        model.addAttribute("agendamento", new Agendamento());
        return "agendamento/cadastro";
    }

    @Operation(summary = "Lista agendamentos por nome do cliente")
    @GetMapping("/listarPorNome")
    @ApiResponse(responseCode = "200", description = "Agendamentos listados com sucesso")
    public String listarAgendamentos(@RequestParam(required = false) String clienteNome, Model model) {
        List<Agendamento> agendamentosPorNome = service.findAllWithDetails(clienteNome);
        model.addAttribute("agendamentos", agendamentosPorNome);
        model.addAttribute("clienteNome", clienteNome); // Adiciona o nome do cliente ao modelo
        return "agendamento/lista_nome";
    }

    @Operation(summary = "Lista todos os agendamentos com seus detalhes")
    @GetMapping("/listar")
    @ApiResponse(responseCode = "200", description = "Agendamentos listados com sucesso")
    public String listar(ModelMap model) {
        List<Agendamento> agendamentos = service.getAll();
        model.addAttribute("agendamentos", agendamentos);
        return "agendamento/lista";
    }

    @Operation(summary = "Salva um novo agendamento")
    @PostMapping("/salvar")
    @ApiResponse(responseCode = "201", description = "Agendamento criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro de validação de dados")
    public String salvar(@Valid @ModelAttribute("agendamento") Agendamento agendamento, BindingResult result, RedirectAttributes attr, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("cliente", clienteService.getAll());
            model.addAttribute("pet", petService.getAll());
            model.addAttribute("servico", servicoService.getAll());
            return "agendamento/cadastro";
        }
        service.create(agendamento);
        attr.addFlashAttribute("success", "Agendamento inserido com sucesso.");
        return "redirect:/agendamentos/listar";
    }

    @Operation(summary = "Exclui um agendamento pelo seu ID")
    @GetMapping("/excluir/{id}")
    @ApiResponse(responseCode = "200", description = "Agendamento excluído com sucesso")
    @ApiResponse(responseCode = "404", description = "Agendamento não encontrado")
    public String excluir(@PathVariable("id") Long id, RedirectAttributes attr) {
        service.delete(id);
        attr.addFlashAttribute("success", "Agendamento excluído com sucesso.");
        return "redirect:/agendamentos/listar";
    }

    @Operation(summary = "Exibe o formulário para editar um agendamento existente")
    @GetMapping("/editar/{id}")
    @ApiResponse(responseCode = "200", description = "Formulário de edição exibido com sucesso")
    @ApiResponse(responseCode = "404", description = "Agendamento não encontrado")
    public String preEditar(@PathVariable("id") Long id, ModelMap model) {
        Agendamento agendamento = service.getById(id).orElseThrow(() -> new IllegalArgumentException("Agendamento não encontrado: " + id));
        model.addAttribute("agendamento", agendamento);
        return "agendamento/editar"; // Nome da página Thymeleaf para edição
    }

    @Operation(summary = "Atualiza um agendamento existente")
    @PostMapping("/editar")
    @ApiResponse(responseCode = "200", description = "Agendamento atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro de validação de dados")
    public String editar(@Valid @ModelAttribute("agendamento") Agendamento agendamento, BindingResult result, RedirectAttributes attr, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("cliente", clienteService.getAll());
            model.addAttribute("pet", petService.getAll());
            model.addAttribute("servico", servicoService.getAll());
            return "agendamento/cadastro";
        }
        service.update(agendamento.getId(), agendamento);
        attr.addFlashAttribute("success", "Agendamento atualizado com sucesso.");
        return "redirect:/agendamentos/listar";
    }

    @Operation(summary = "Mostra por dia os clientes agendados")
    @GetMapping("/horariosMaisAgendados")
    @ApiResponse(responseCode = "200", description = "Horários mais agendados listados com sucesso")
    public String listarHorariosMaisAgendados(Model model) {
        Map<LocalDate, List<String>> contagemHorarios = service.contarAgendamentosPorDia();
        model.addAttribute("agendamentosPorDia", contagemHorarios);
        return "agendamento/horarios"; // Nome da página Thymeleaf onde os dados serão exibidos
    }

    @ModelAttribute("cliente")
    public List<Cliente> getClientes() {
        return clienteService.getAll();
    }

    @ModelAttribute("pet")
    public List<Pet> getPet() {
        return petService.getAll();
    }

    @ModelAttribute("servico")
    public List<Servico> getServico() {
        return servicoService.getAll();
    }
}