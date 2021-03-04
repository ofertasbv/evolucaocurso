/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.oferta.api.service.serviceImpl;

import com.br.oferta.api.model.Contato;
import com.br.oferta.api.util.filter.ContatoFilter;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author fabio
 */
public interface ContatoServiceImpl {

    List<Contato> findBySort();

    Optional<Contato> findById(Long id);

    Page<Contato> filtrar(ContatoFilter filtro, Pageable pageable);

    Contato create(Contato c);

    Contato update(Long id, Contato c);

    ResponseEntity delete(Long id);

    long count();

}
