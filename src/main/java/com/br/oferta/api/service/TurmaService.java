/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.oferta.api.service;

import com.br.oferta.api.model.Turma;
import com.br.oferta.api.repository.TurmaRepository;
import com.br.oferta.api.service.serviceImpl.TurmaServiceImpl;
import com.br.oferta.api.util.exception.NomeTurmaJaCadastradoException;
import com.br.oferta.api.util.filter.TurmaFilter;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class TurmaService implements TurmaServiceImpl {

    private final TurmaRepository turmaRepository;

    @PersistenceContext
    private final EntityManager em;

    private final PaginacaoUtil paginacaoUtil;

    private static final Logger logger = LoggerFactory.getLogger(TurmaService.class);

    @Autowired
    public TurmaService(TurmaRepository turmaRepository, PaginacaoUtil paginacaoUtil, EntityManager em) {
        this.turmaRepository = turmaRepository;
        this.paginacaoUtil = paginacaoUtil;
        this.em = em;
    }

    @Override
    public long count() {
        Query query = em.createQuery("SELECT COUNT(c) FROM Turma c");
        return (long) query.getSingleResult();
    }

    @Override
    public List<Turma> findBySort() {
        Query query = em.createQuery("SELECT c FROM Turma c");
        return query.getResultList();
    }

    @Override
    public Turma findById(Long id) {
        return turmaRepository.findById(id).get();
    }

    @Override
    public Turma findByCursoById(Long id) {
        Query query = em.createQuery("SELECT p FROM Turma p JOIN p.curso o WHERE o.id =:id");
        query.setParameter("id", id);
        return (Turma) query.getSingleResult();
    }

    @Override
    public Turma create(Turma c) {
        LocalDate dataRegistro = LocalDate.now();
        c.setDataRegistro(dataRegistro);

        Optional<Turma> optional = turmaRepository.findByNomeIgnoreCase(c.getNome());
        if (optional.isPresent()) {
            throw new NomeTurmaJaCadastradoException("Nome da turma já cadastrado");
        }
        return turmaRepository.save(c);
    }

    @Override
    public Turma update(Long id, Turma c) {
        return turmaRepository.save(c);
    }

    @Override
    public ResponseEntity delete(Long id) {
        Optional<Turma> existe = turmaRepository.findById(id);
        if (existe.isPresent()) {
            throw new EmptyResultDataAccessException(1);
        }

        turmaRepository.deleteById(id);
        return ResponseEntity.ok(existe);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Turma> filtrar(TurmaFilter filtro, Pageable pageable) {
        Criteria criteria = em.unwrap(Session.class).createCriteria(Turma.class);

        paginacaoUtil.preparar(criteria, pageable);
        adicionarFiltro(filtro, criteria);

        return new PageImpl<>(criteria.list(), pageable, total(filtro));
    }

    private Long total(TurmaFilter filtro) {
        Criteria criteria = em.unwrap(Session.class).createCriteria(Turma.class);
        adicionarFiltro(filtro, criteria);
        criteria.setProjection(Projections.rowCount());
        return (Long) criteria.uniqueResult();
    }

    private void adicionarFiltro(TurmaFilter filtro, Criteria criteria) {
        if (filtro != null) {

            if (!StringUtils.isEmpty(filtro.getDescricao())) {
                criteria.add(Restrictions.ilike("descricao", filtro.getDescricao().toLowerCase(), MatchMode.ANYWHERE));
            }

            if (filtro.getTurno() != null) {
                criteria.add(Restrictions.eq("turno", filtro.getTurno()));
            }

            if (filtro.getCurso() != null) {
                criteria.add(Restrictions.eq("curso", filtro.getCurso()));
            }

            if (filtro.getDesde() != null) {
                LocalDateTime desde = LocalDateTime.of(filtro.getDesde(), LocalTime.of(0, 0));
                criteria.add(Restrictions.ge("dataInicio", desde));
            }

            if (filtro.getAte() != null) {
                LocalDateTime ate = LocalDateTime.of(filtro.getAte(), LocalTime.of(23, 59));
                criteria.add(Restrictions.le("dataRegistro", ate));
            }
        }
    }

}
