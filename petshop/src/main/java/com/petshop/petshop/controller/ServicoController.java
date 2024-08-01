package com.petshop.petshop.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
import com.petshop.petshop.model.Servico;
import com.petshop.petshop.service.ServicoService;

@Controller
@RequestMapping("/servicos")
@Tag(name = "Serviço", description = "Cadastro e gerenciamento de Serviços")
public class ServicoController {

    @Autowired
    private ServicoService service;

    @Operation(summary = "Exibe o formulário para criar um novo serviço")
    @GetMapping("/cadastrar")
    @ApiResponse(responseCode = "200", description = "Formulário exibido com sucesso")
    public String cadastrar(Model model) {
        model.addAttribute("servico", new Servico());
        return "servico/cadastro";
    }

    @Operation(summary = "Lista todos os serviços")
    @GetMapping("/listar")
    @ApiResponse(responseCode = "200", description = "Lista de serviços exibida com sucesso")
    public String listar(ModelMap model) {
        model.addAttribute("servico", service.getAll());
        return "servico/lista";
    }

    @Operation(summary = "Salva um novo serviço")
    @PostMapping("/salvar")
    @ApiResponse(responseCode = "201", description = "Serviço criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro de validação de dados")
    public String salvar(@Valid @ModelAttribute Servico servico, BindingResult result, RedirectAttributes attr) {
        if (result.hasErrors()) {
            return "servico/cadastro";
        }
        try {
            service.create(servico);
            attr.addFlashAttribute("success", "Serviço inserido com sucesso.");
        } catch (DataIntegrityViolationException e) {
            attr.addFlashAttribute("fail", "Erro ao inserir o serviço: " + e.getMessage());
        }
        return "redirect:/servicos/listar";
    }

    @Operation(summary = "Exclui um serviço pelo seu ID")
    @GetMapping("/excluir/{id}")
    @ApiResponse(responseCode = "200", description = "Serviço excluído com sucesso")
    @ApiResponse(responseCode = "404", description = "Serviço não encontrado")
    public String excluir(@PathVariable("id") Long id, RedirectAttributes attr) {
        service.delete(id);
        attr.addFlashAttribute("success", "Serviço excluído com sucesso.");
        return "redirect:/servicos/listar";
    }

    @Operation(summary = "Exibe o formulário para editar um serviço existente")
    @GetMapping("/editar/{id}")
    @ApiResponse(responseCode = "200", description = "Formulário de edição exibido com sucesso")
    @ApiResponse(responseCode = "404", description = "Serviço não encontrado")
    public String preEditar(@PathVariable("id") Long id, ModelMap model) {
        Servico servico = service.getById(id)
                .orElseThrow(() -> new IllegalArgumentException("Serviço não encontrado: " + id));
        model.addAttribute("servico", servico);
        return "servico/editar";
    }

    @Operation(summary = "Atualiza um serviço existente")
    @PostMapping("/editar")
    @ApiResponse(responseCode = "200", description = "Serviço atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro de validação de dados")
    public String editar(@Valid @ModelAttribute("servico") Servico servico, BindingResult result, RedirectAttributes attr) {
        if (result.hasErrors()) {
            return "servico/editar";
        }
        try {
            service.update(servico.getId(), servico);
            attr.addFlashAttribute("success", "Serviço atualizado com sucesso.");
        } catch (DataIntegrityViolationException e) {
            attr.addFlashAttribute("fail", "Erro ao atualizar o serviço: " + e.getMessage());
        }
        return "redirect:/servicos/listar";
    }
}
