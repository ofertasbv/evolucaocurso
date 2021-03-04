/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.oferta.api.service.serviceImpl;

import com.br.oferta.api.model.Categoria;
import com.br.oferta.api.model.Matricula;
import com.br.oferta.api.util.filter.MatriculaFilter;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author fabio
 */
public interface MatriculaServiceImpl {

    List<Matricula> findBySort();

    Optional<Matricula> findById(Long id);

    List<Matricula> findByTurmaById(Long id);

    Matricula findByCursoById(Long id);

    Page<Matricula> filtrar(MatriculaFilter filtro, Pageable pageable);

    Matricula create(Matricula c);

    Matricula update(Long id, Matricula c);

    ResponseEntity delete(Long id);

    long count();
}
