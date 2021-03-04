/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.oferta.api.service.serviceImpl;

import com.br.oferta.api.model.Grupo;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author fabio
 */
public interface GrupoServiceImpl {

    List<Grupo> findBySort();

    Optional<Grupo> findById(Long id);

    Grupo create(Grupo c);

    Grupo update(Long id, Grupo c);

    ResponseEntity delete(Long id);

    long count();

}