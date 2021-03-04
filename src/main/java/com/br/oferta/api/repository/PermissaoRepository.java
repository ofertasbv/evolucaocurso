package com.br.oferta.api.repository;

import com.br.oferta.api.model.Permissao;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissaoRepository extends JpaRepository<Permissao, Long> {

    public Optional<Permissao> findByDescricaoIgnoreCase(String descricao);
}
