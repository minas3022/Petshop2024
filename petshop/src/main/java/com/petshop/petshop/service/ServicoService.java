
package com.petshop.petshop.service;

import com.petshop.petshop.model.Servico;
import com.petshop.petshop.repository.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServicoService {
    @Autowired
    private ServicoRepository repository;

    public List<Servico> getAll() {
        return repository.findAll();
    }

    public Optional<Servico> getById(Long id) {
        return repository.findById(id);
    }

    public Servico create(Servico servico) {
        return repository.save(servico);
    }

    public Optional<Servico> update(Long id, Servico servico) {
        if (!repository.existsById(id)) {
            return Optional.empty();
        }
        servico.setId(id);
        Servico updatedServico = repository.save(servico);
        return Optional.of(updatedServico);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
