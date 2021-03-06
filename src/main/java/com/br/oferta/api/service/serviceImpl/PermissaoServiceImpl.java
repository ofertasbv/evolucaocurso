/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.oferta.api.service.serviceImpl;

import com.br.oferta.api.model.Permissao;
import com.br.oferta.api.util.filter.PermissaoFilter;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author fabio
 */
public interface PermissaoServiceImpl {

    List<Permissao> findBySort();

    Optional<Permissao> findById(Long id);

    Page<Permissao> filtrar(PermissaoFilter filtro, Pageable pageable);

    Permissao create(Permissao p);

    Permissao update(Long id, Permissao p);

    ResponseEntity delete(Long id);

    long count();
}
