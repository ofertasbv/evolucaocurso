package com.br.oferta.api.repository;

import com.br.oferta.api.model.Matricula;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatriculaRepository extends JpaRepository<Matricula, Long> {

    public Optional<Matricula> findByDescricaoIgnoreCase(String descricao);
}
