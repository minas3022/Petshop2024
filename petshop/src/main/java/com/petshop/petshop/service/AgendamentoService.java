
package com.petshop.petshop.service;

import com.petshop.petshop.model.Agendamento;
import com.petshop.petshop.repository.AgendamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class AgendamentoService {
    @Autowired
    private AgendamentoRepository repository;

    public Optional<Agendamento> getById(Long id) {
        return repository.findById(id);
    }

    public Agendamento create(Agendamento agendamento) {
        return repository.save(agendamento);
    }

    public List<Agendamento> getAll(){
        return  repository.findAll();
    }

    public Optional<Agendamento> update(Long id, Agendamento agendamento) {
        if (!repository.existsById(id)) {
            return Optional.empty();
        }
        agendamento.setId(id); // Garantindo que o ID seja preservado
        Agendamento updatedAgndamento = repository.save(agendamento);
        return Optional.of(updatedAgndamento);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Map<LocalDate, List<String>> contarAgendamentosPorDia() {
        List<Agendamento> agendamentos = repository.findAll();
        Map<LocalDate, List<String>> agendamentosPorDia = new HashMap<>();

        for (Agendamento agendamento : agendamentos) {
            LocalDate data = agendamento.getDataHora().toLocalDate();
            String paciente = agendamento.getCliente().getNome(); // Supondo que você tenha um método getPaciente() que retorna o paciente do agendamento
            String horario = agendamento.getDataHora().toLocalTime().toString(); // Horário do agendamento

            if (!agendamentosPorDia.containsKey(data)) {
                agendamentosPorDia.put(data, new ArrayList<>());
            }

            agendamentosPorDia.get(data).add(paciente + " - " + horario);
        }

        return agendamentosPorDia;
    }

    public List<Agendamento> findAllWithDetails(String clienteNome) {
        return repository.findByClienteNomeContaining(clienteNome);
    }
}