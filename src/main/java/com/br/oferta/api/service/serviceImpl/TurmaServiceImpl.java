/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.oferta.api.service.serviceImpl;

import com.br.oferta.api.model.Categoria;
import com.br.oferta.api.model.Turma;
import com.br.oferta.api.util.filter.TurmaFilter;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author fabio
 */
public interface TurmaServiceImpl {

    List<Turma> findBySort();

    Turma findById(Long id);

    Page<Turma> filtrar(TurmaFilter filtro, Pageable pageable);

    Turma findByCursoById(Long id);

    Turma create(Turma c);

    Turma update(Long id, Turma c);

    ResponseEntity delete(Long id);

    long count();

}
