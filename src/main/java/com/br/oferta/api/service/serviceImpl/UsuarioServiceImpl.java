/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.oferta.api.service.serviceImpl;

import com.br.oferta.api.model.Usuario;
import com.br.oferta.api.util.filter.UsuarioFilter;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author fabio
 */
public interface UsuarioServiceImpl {

    List<Usuario> findBySort();

    Optional<Usuario> findById(Long id);

    public Optional<Usuario> findByEmail(String email);

    public Optional<Usuario> findByCpf(String cpf);

    public List<String> permissoes(Usuario usuario);

    Page<Usuario> filtrar(UsuarioFilter filtro, Pageable pageable);

    public Usuario buscarComGrupos(Long id);

    Usuario create(Usuario u);

    ResponseEntity update(Long id, Usuario u);

    ResponseEntity delete(Long id);

    long count();
}
