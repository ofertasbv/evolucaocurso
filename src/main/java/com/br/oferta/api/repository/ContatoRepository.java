package com.br.oferta.api.repository;

import com.br.oferta.api.model.Contato;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContatoRepository extends JpaRepository<Contato, Long> {

}
