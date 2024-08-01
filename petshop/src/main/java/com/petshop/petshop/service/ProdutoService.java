
package com.petshop.petshop.service;

import com.petshop.petshop.model.Produto;
import com.petshop.petshop.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {
    @Autowired
    private ProdutoRepository repository;

    public Page<Produto> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Optional<Produto> getById(Long id) {
        return repository.findById(id);
    }

    public Produto create(Produto produto) {
        return repository.save(produto);
    }

    public Optional<Produto> update(Long id, Produto produto) {
        if (!repository.existsById(id)) {
            return Optional.empty();
        }
        produto.setId(id);
        Produto updatedProduto = repository.save(produto);
        return Optional.of(updatedProduto );
    }
    public Page<Produto> findPage(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }


    public List<Produto> findAll() {
        return repository.findAll();
    }

    public Page<Produto> findByNamePage(String nome, Pageable pageable) {
        return repository.findByNome(nome, pageable);
    }
}
