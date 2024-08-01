package com.petshop.petshop.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import com.petshop.petshop.model.Produto;
import com.petshop.petshop.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public class ProdutoServiceTest {

    @Mock
    private ProdutoRepository repository;

    @InjectMocks
    private ProdutoService produtoService;

    private Produto produto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        produto = new Produto();
        produto.setId(1L);
        produto.setNome("Produto Teste");
        // Defina outros campos conforme necessário
    }

    @Test
    public void testGetAll() {
        Page<Produto> produtosPage = new PageImpl<>(List.of(produto));
        when(repository.findAll(any(Pageable.class))).thenReturn(produtosPage);

        Page<Produto> result = produtoService.getAll(Pageable.unpaged());

        assertFalse(result.isEmpty());
        verify(repository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    public void testGetById() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(produto));

        Optional<Produto> result = produtoService.getById(1L);

        assertTrue(result.isPresent());
        assertEquals(produto.getNome(), result.get().getNome());
        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    public void testCreate() {
        when(repository.save(any(Produto.class))).thenReturn(produto);

        Produto result = produtoService.create(produto);

        assertEquals(produto.getNome(), result.getNome());
        verify(repository, times(1)).save(any(Produto.class));
    }

    @Test
    public void testUpdate() {
        when(repository.existsById(anyLong())).thenReturn(true);
        when(repository.save(any(Produto.class))).thenReturn(produto);

        Optional<Produto> result = produtoService.update(1L, produto);

        assertTrue(result.isPresent());
        assertEquals(produto.getNome(), result.get().getNome());
        verify(repository, times(1)).existsById(anyLong());
        verify(repository, times(1)).save(any(Produto.class));
    }

    @Test
    public void testUpdateNotFound() {
        when(repository.existsById(anyLong())).thenReturn(false);

        Optional<Produto> result = produtoService.update(1L, produto);

        assertFalse(result.isPresent());
        verify(repository, times(1)).existsById(anyLong());
    }

    @Test
    public void testDelete() {
        doNothing().when(repository).deleteById(anyLong());

        produtoService.delete(1L);

        verify(repository, times(1)).deleteById(anyLong());
    }
}
