package com.br.oferta.api.repository;

import com.br.oferta.api.model.Categoria;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    public Optional<Categoria> findByNomeIgnoreCase(String nome);
}
