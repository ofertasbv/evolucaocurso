package com.br.oferta.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.oferta.api.model.Grupo;
import java.util.Optional;

public interface GrupoRepository extends JpaRepository<Grupo, Long> {

    public Optional<Grupo> findByNomeIgnoreCase(String nome);
}
