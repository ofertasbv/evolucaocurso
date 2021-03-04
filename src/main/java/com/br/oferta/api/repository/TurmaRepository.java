package com.br.oferta.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.oferta.api.model.Turma;
import java.util.Optional;

public interface TurmaRepository extends JpaRepository<Turma, Long> {
    
    public Optional<Turma> findByNomeIgnoreCase(String nome);
}
