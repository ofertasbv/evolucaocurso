/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.oferta.api.service;

import com.br.oferta.api.model.Grupo;
import com.br.oferta.api.repository.GrupoRepository;
import com.br.oferta.api.service.serviceImpl.GrupoServiceImpl;
import com.br.oferta.api.util.exception.NomeGrupoJaCadastradoException;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 *
 * @author fabio
 */
@Service
public class GrupoService implements GrupoServiceImpl {

    private final GrupoRepository grupoRepository;

    @PersistenceContext
    private final EntityManager em;

    @Autowired
    public GrupoService(GrupoRepository grupoRepository, EntityManager em) {
        this.grupoRepository = grupoRepository;
        this.em = em;
    }

    @Override
    public long count() {
        Query query = em.createQuery("SELECT COUNT(c) FROM Grupo c");
        return (long) query.getSingleResult();
    }

    @Override
    public List<Grupo> findBySort() {
        Query query = em.createQuery("SELECT a FROM Grupo a ORDER BY a.id ASC");
        return query.getResultList();
    }

    @Override
    public Optional<Grupo> findById(Long id) {
        return grupoRepository.findById(id);
    }

    @Override
    public Grupo create(Grupo a) {
        Optional<Grupo> optional = grupoRepository.findByNomeIgnoreCase(a.getNome());
        if (optional.isPresent()) {
            throw new NomeGrupoJaCadastradoException("Nome do grupo já cadastrado");
        }
        return grupoRepository.save(a);
    }

    @Override
    public Grupo update(Long id, Grupo a) {
        return grupoRepository.save(a);
    }

    @Override
    public ResponseEntity delete(Long id) {
        Grupo exclui = grupoRepository.findById(id).get();
        if (exclui == null) {
            throw new EmptyResultDataAccessException(1);
        }

        grupoRepository.deleteById(id);
        return ResponseEntity.ok(exclui);
    }

}
