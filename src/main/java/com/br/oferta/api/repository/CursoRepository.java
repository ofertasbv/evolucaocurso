package com.br.oferta.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.oferta.api.model.Curso;

public interface CursoRepository extends JpaRepository<Curso, Long>{
  
}
