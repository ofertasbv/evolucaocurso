/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.oferta.api.service;

import com.br.oferta.api.service.serviceImpl.PermissaoServiceImpl;
import com.br.oferta.api.model.Permissao;
import com.br.oferta.api.repository.PermissaoRepository;
import com.br.oferta.api.util.exception.NomeGrupoJaCadastradoException;
import com.br.oferta.api.util.filter.PermissaoFilter;
import com.br.oferta.api.util.paginacao.PaginacaoUtil;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 *
 * @author fabio
 */
@Service
public class PermissaoService implements PermissaoServiceImpl {

    private final PermissaoRepository permissaoRepository;

    private final PaginacaoUtil paginacaoUtil;

    @PersistenceContext
    private final EntityManager em;

    @Autowired
    public PermissaoService(PermissaoRepository permissaoRepository, PaginacaoUtil paginacaoUtil, EntityManager em) {
        this.permissaoRepository = permissaoRepository;
        this.paginacaoUtil = paginacaoUtil;
        this.em = em;
    }

    @Override
    public long count() {
        Query query = em.createQuery("SELECT COUNT(c) FROM Permissao c");
        return (long) query.getSingleResult();
    }

    @Override
    public List<Permissao> findBySort() {
        Query query = em.createQuery("SELECT a FROM Permissao a ORDER BY a.id DESC");
        return query.getResultList();
    }

    @Override
    public Optional<Permissao> findById(Long id) {
        return permissaoRepository.findById(id);
    }

    @Override
    public Permissao create(Permissao p) {
        Optional<Permissao> optional = permissaoRepository.findByDescricaoIgnoreCase(p.getDescricao());
        if (optional.isPresent()) {
            throw new NomeGrupoJaCadastradoException("Descrição da permissão já cadastrado");
        }
        return permissaoRepository.save(p);
    }

    @Override
    public Permissao update(Long id, Permissao p) {
        return permissaoRepository.save(p);
    }

    @Override
    public ResponseEntity delete(Long id) {
        Permissao exclui = permissaoRepository.findById(id).get();
        if (exclui == null) {
            throw new EmptyResultDataAccessException(1);
        }

        permissaoRepository.deleteById(id);
        permissaoRepository.flush();
        return ResponseEntity.ok(exclui);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Permissao> filtrar(PermissaoFilter filtro, Pageable pageable) {
        Criteria criteria = em.unwrap(Session.class).createCriteria(Permissao.class);

        paginacaoUtil.preparar(criteria, pageable);
        adicionarFiltro(filtro, criteria);

        return new PageImpl<>(criteria.list(), pageable, total(filtro));
    }

    private Long total(PermissaoFilter filtro) {
        Criteria criteria = em.unwrap(Session.class).createCriteria(Permissao.class);
        adicionarFiltro(filtro, criteria);
        criteria.setProjection(Projections.rowCount());
        return (Long) criteria.uniqueResult();
    }

    private void adicionarFiltro(PermissaoFilter filtro, Criteria criteria) {
        if (filtro != null) {

            if (!StringUtils.isEmpty(filtro.getDescricao())) {
                criteria.add(Restrictions.ilike("descricao", filtro.getDescricao().toLowerCase(), MatchMode.ANYWHERE));
            }

            if (filtro.getGrupo() != null) {
                criteria.createAlias("grupos", "grupo");
                criteria.add(Restrictions.eq("grupo", filtro.getGrupo()));
            }
        }
    }
}
