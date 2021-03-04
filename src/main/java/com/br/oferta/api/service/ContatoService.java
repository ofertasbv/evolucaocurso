/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.oferta.api.service;

import com.br.oferta.api.model.Contato;
import com.br.oferta.api.repository.ContatoRepository;
import com.br.oferta.api.service.serviceImpl.ContatoServiceImpl;
import com.br.oferta.api.util.filter.ContatoFilter;
import com.br.oferta.api.util.paginacao.PaginacaoUtil;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
public class ContatoService implements ContatoServiceImpl {

    private final ContatoRepository contatoRepository;

    @PersistenceContext
    private final EntityManager em;

    private final PaginacaoUtil paginacaoUtil;

    @Autowired
    public ContatoService(ContatoRepository contatoRepository, PaginacaoUtil paginacaoUtil, EntityManager em) {
        this.contatoRepository = contatoRepository;
        this.paginacaoUtil = paginacaoUtil;
        this.em = em;
    }

    @Override
    public long count() {
        Query query = em.createQuery("SELECT COUNT(c) FROM Contato c");
        return (long) query.getSingleResult();
    }

    @Override
    public List<Contato> findBySort() {
        Query query = em.createQuery("SELECT a FROM Contato a ORDER BY a.id ASC");
        return query.getResultList();
    }

    @Override
    public Optional<Contato> findById(Long id) {
        return contatoRepository.findById(id);
    }

    @Override
    public Contato create(Contato a) {
        LocalDate dataRegistro = LocalDate.now();
        a.setDataRegistro(dataRegistro);
        return contatoRepository.save(a);
    }

    @Override
    public Contato update(Long id, Contato a) {
        return contatoRepository.save(a);
    }

    @Override
    public ResponseEntity delete(Long id) {
        Contato exclui = contatoRepository.findById(id).get();
        if (exclui == null) {
            throw new EmptyResultDataAccessException(1);
        }

        contatoRepository.deleteById(id);
        return ResponseEntity.ok(exclui);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Contato> filtrar(ContatoFilter filtro, Pageable pageable) {
        Criteria criteria = em.unwrap(Session.class).createCriteria(Contato.class);

        paginacaoUtil.preparar(criteria, pageable);
        adicionarFiltro(filtro, criteria);

        return new PageImpl<>(criteria.list(), pageable, total(filtro));
    }

    private Long total(ContatoFilter filtro) {
        Criteria criteria = em.unwrap(Session.class).createCriteria(Contato.class);
        adicionarFiltro(filtro, criteria);
        criteria.setProjection(Projections.rowCount());
        return (Long) criteria.uniqueResult();
    }

    private void adicionarFiltro(ContatoFilter filtro, Criteria criteria) {
        if (filtro != null) {

            if (!StringUtils.isEmpty(filtro.getNome())) {
                criteria.add(Restrictions.ilike("nome", filtro.getNome().toLowerCase(), MatchMode.ANYWHERE));
            }

            if (!StringUtils.isEmpty(filtro.getEmail())) {
                criteria.add(Restrictions.ilike("email", filtro.getEmail().toLowerCase(), MatchMode.ANYWHERE));
            }

            if (!StringUtils.isEmpty(filtro.getDescricao())) {
                criteria.add(Restrictions.ilike("descricao", filtro.getDescricao().toLowerCase(), MatchMode.ANYWHERE));
            }

            if (filtro.getDesde() != null) {
                LocalDateTime desde = LocalDateTime.of(filtro.getDesde(), LocalTime.of(0, 0));
                criteria.add(Restrictions.ge("dataRegistro", desde));
            }

            if (filtro.getAte() != null) {
                LocalDateTime ate = LocalDateTime.of(filtro.getAte(), LocalTime.of(23, 59));
                criteria.add(Restrictions.le("dataRegistro", ate));
            }
        }
    }

}
