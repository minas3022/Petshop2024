
package com.petshop.petshop.repository;

import com.petshop.petshop.model.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto,Long> {
    Page<Produto> findAll(Pageable pageable);

    Page<Produto> findByNome(String nome, Pageable pageable);
}
