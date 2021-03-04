/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.oferta.api.service;

import com.br.oferta.api.model.Matricula;
import com.br.oferta.api.repository.MatriculaRepository;
import com.br.oferta.api.service.serviceImpl.MatriculaServiceImpl;
import com.br.oferta.api.util.exception.DescricaoMatriculaJaCadastradoException;
import com.br.oferta.api.util.filter.MatriculaFilter;
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
public class MatriculaService implements MatriculaServiceImpl {

    private final MatriculaRepository matriculaRepository;

    @PersistenceContext
    private final EntityManager em;

    private final PaginacaoUtil paginacaoUtil;

    private static final Logger logger = LoggerFactory.getLogger(MatriculaService.class);

    @Autowired
    public MatriculaService(MatriculaRepository matriculaRepository, PaginacaoUtil paginacaoUtil, EntityManager em) {
        this.matriculaRepository = matriculaRepository;
        this.paginacaoUtil = paginacaoUtil;
        this.em = em;
    }

    @Override
    public long count() {
        Query query = em.createQuery("SELECT COUNT(c) FROM Matricula c");
        return (long) query.getSingleResult();
    }

    @Override
    public List<Matricula> findBySort() {
        Query query = em.createQuery("SELECT c FROM Matricula c");
        return query.getResultList();
    }

    @Override
    public Optional<Matricula> findById(Long id) {
        return matriculaRepository.findById(id);
    }

    @Override
    public Matricula findByCursoById(Long id) {
        Query query = em.createQuery("SELECT p FROM Matricula p JOIN p.turma o WHERE o.id =:id");
        query.setParameter("id", id);
        return (Matricula) query.getSingleResult();
    }

    @Override
    public List<Matricula> findByTurmaById(Long id) {
        Query query = em.createQuery("SELECT p FROM Matricula p JOIN p.turma o WHERE o.id =:id");
        query.setParameter("id", id);
        return query.getResultList();
    }

    @Override
    public Matricula create(Matricula c) {
        LocalDate dataRegistro = LocalDate.now();
        c.setDataRegistro(dataRegistro);

        Optional<Matricula> optional = matriculaRepository.findByDescricaoIgnoreCase(c.getDescricao());
        if (optional.isPresent()) {
            throw new DescricaoMatriculaJaCadastradoException("A descrição da matrícula já cadastrado");
        }
        return matriculaRepository.save(c);
    }

    @Override
    public Matricula update(Long id, Matricula c) {
        return matriculaRepository.save(c);
    }

    @Override
    public ResponseEntity delete(Long id) {
        Optional<Matricula> existe = matriculaRepository.findById(id);
        if (existe.isPresent()) {
            throw new EmptyResultDataAccessException(1);
        }

        matriculaRepository.deleteById(id);
        return ResponseEntity.ok(existe);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Matricula> filtrar(MatriculaFilter filtro, Pageable pageable) {
        Criteria criteria = em.unwrap(Session.class).createCriteria(Matricula.class);

        paginacaoUtil.preparar(criteria, pageable);
        adicionarFiltro(filtro, criteria);

        return new PageImpl<>(criteria.list(), pageable, total(filtro));
    }

    private Long total(MatriculaFilter filtro) {
        Criteria criteria = em.unwrap(Session.class).createCriteria(Matricula.class);
        adicionarFiltro(filtro, criteria);
        criteria.setProjection(Projections.rowCount());
        return (Long) criteria.uniqueResult();
    }

    private void adicionarFiltro(MatriculaFilter filtro, Criteria criteria) {
        if (filtro != null) {

            if (!StringUtils.isEmpty(filtro.getDescricao())) {
                criteria.add(Restrictions.ilike("descricao", filtro.getDescricao().toLowerCase(), MatchMode.ANYWHERE));
            }

            if (filtro.getTurma() != null) {
                criteria.add(Restrictions.eq("turma", filtro.getTurma()));
            }

            if (filtro.getUsuario() != null) {
                criteria.add(Restrictions.eq("usuario", filtro.getUsuario()));
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
