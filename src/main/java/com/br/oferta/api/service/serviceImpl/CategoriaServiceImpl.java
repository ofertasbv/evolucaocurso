/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.oferta.api.service.serviceImpl;

import com.br.oferta.api.model.Categoria;
import com.br.oferta.api.util.filter.CategoriaFilter;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author fabio
 */
public interface CategoriaServiceImpl {

    List<Categoria> findBySort();

    List<Categoria> findAll();

    Optional<Categoria> findById(Long id);

    List<Categoria> findAllByNome(String nome);

    Page<Categoria> filtrar(CategoriaFilter filtro, Pageable pageable);

    Categoria create(Categoria c);

    Categoria update(Long id, Categoria c);

    ResponseEntity delete(Long id);

    void excluirFoto(String foto);

    long count();

}
