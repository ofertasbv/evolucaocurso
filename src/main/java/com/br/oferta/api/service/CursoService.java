/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.oferta.api.service;

import com.br.oferta.api.model.Curso;
import com.br.oferta.api.util.error.ServiceNotFoundExeception;
import com.br.oferta.api.util.filter.CursoFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.br.oferta.api.repository.CursoRepository;
import com.br.oferta.api.service.serviceImpl.CursoServiceImpl;
import com.br.oferta.api.util.paginacao.PaginacaoUtil;
import java.time.LocalDate;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.data.domain.PageImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 *
 * @author fabio
 */
@Service
public class CursoService implements CursoServiceImpl {

    private final CursoRepository produtoRepository;

    private final PaginacaoUtil paginacaoUtil;

    @PersistenceContext
    private final EntityManager em;

    @Value("${contato.disco.raiz}")
    private java.nio.file.Path local;

    private static final Logger logger = LoggerFactory.getLogger(CursoService.class);

    @Autowired
    public CursoService(CursoRepository produtoRepository, PaginacaoUtil paginacaoUtil, EntityManager em) {
        this.produtoRepository = produtoRepository;
        this.paginacaoUtil = paginacaoUtil;
        this.em = em;
    }

    @Override
    public Curso create(Curso p) {
        LocalDate dataRegistro = LocalDate.now();
        p.setDataRegistro(dataRegistro);
        return produtoRepository.saveAndFlush(p);
    }

    @Override
    public ResponseEntity update(Long id, Curso p) {
        Curso produtoSalva = produtoRepository.findById(id).get();
        if (produtoSalva == null) {
            throw new ServiceNotFoundExeception("Produto não encotrado com ID: " + id);
        }
        BeanUtils.copyProperties(p, produtoSalva, "id");
        produtoRepository.save(produtoSalva);
        return ResponseEntity.ok(produtoSalva);
    }

    @Override
    public ResponseEntity delete(Long id) {
        Curso produtoExclui = produtoRepository.findById(id).get();
        if (produtoExclui == null) {
            throw new EmptyResultDataAccessException(1);
        }

        produtoRepository.deleteById(id);
        return ResponseEntity.ok(produtoExclui);
    }

    @Override
    public void excluirFoto(String foto) {
        try {
            Files.deleteIfExists(this.local.resolve(foto));
        } catch (IOException e) {
            logger.warn(String.format("Erro apagando foto '%s'. Mensagem: %s", foto, e.getMessage()));
        }
    }

    @Override
    public long count() {
        Query query = em.createQuery("SELECT COUNT(c) FROM Curso c");
        return (long) query.getSingleResult();
    }

    @Override
    public List<Curso> findAll() {
        Query query = em.createQuery("SELECT p FROM Curso p");
        return query.getResultList();
    }

    @Override
    public Optional<Curso> findById(Long id) {
        return produtoRepository.findById(id);
    }

    @Override
    public Page<Curso> findAllByPage(Pageable pageable) {
        return produtoRepository.findAll(pageable);
    }

    @Override
    public List<Curso> findByNome(String nome) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Curso> query = criteriaBuilder.createQuery(Curso.class);
        Root<Curso> n = query.from(Curso.class);

        Path<String> nomePath = n.<String>get("nome");
        List<Predicate> predicates = new ArrayList<>();

        if (nome != null) {
            Predicate paramentro = criteriaBuilder.like(criteriaBuilder.lower(nomePath), "%" + nome.toLowerCase() + "%");
            predicates.add(paramentro);
        }

        query.where((Predicate[]) predicates.toArray(new Predicate[0]));
        query.orderBy(criteriaBuilder.desc(n.get("id")));
        TypedQuery<Curso> typedQuery = em.createQuery(query);

        return typedQuery.getResultList();
    }

    @Override
    public List<Curso> findBySubCategoriaById(Long id) {
        Query query = em.createQuery("SELECT p FROM Curso p JOIN p.categoria c WHERE c.id =:id");
        query.setParameter("id", id);
        return query.getResultList();
    }

    @Override
    public List<Curso> findByPessoaById(Long id) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Curso> query = criteriaBuilder.createQuery(Curso.class);
        Root<Curso> n = query.from(Curso.class);
        Path<Long> promocaoCodigoPath = n.join("promocao").<Long>get("id");
        List<Predicate> predicates = new ArrayList<>();
        if (id != null) {
            Predicate paramentro = criteriaBuilder.le(promocaoCodigoPath, id);
            predicates.add(paramentro);
        }
        query.where((Predicate[]) predicates.toArray(new Predicate[0]));
        query.orderBy(criteriaBuilder.desc(n.get("id")));
        TypedQuery<Curso> typedQuery = em.createQuery(query);
        return typedQuery.getResultList();
    }

    @Override
    public List<Curso> findByDestaque(boolean destaque) {
        Query query = em.createQuery("SELECT p FROM Curso p WHERE p.destaque =:destaque");
        query.setParameter("destaque", destaque);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Curso> filtrar(CursoFilter filtro, Pageable pageable) {
        Criteria criteria = em.unwrap(Session.class).createCriteria(Curso.class);

        paginacaoUtil.preparar(criteria, pageable);
        adicionarFiltro(filtro, criteria);

        return new PageImpl<>(criteria.list(), pageable, total(filtro));
    }

    private Long total(CursoFilter filtro) {
        Criteria criteria = em.unwrap(Session.class).createCriteria(Curso.class);
        adicionarFiltro(filtro, criteria);
        criteria.setProjection(Projections.rowCount());
        return (Long) criteria.uniqueResult();
    }

    private void adicionarFiltro(CursoFilter filtro, Criteria criteria) {
        if (filtro != null) {

            if (!StringUtils.isEmpty(filtro.getNome())) {
                criteria.add(Restrictions.ilike("nome", filtro.getNome().toLowerCase(), MatchMode.ANYWHERE));
            }

            if (filtro.getModalidade() != null) {
                criteria.add(Restrictions.eq("modalidade", filtro.getModalidade()));
            }

            if (filtro.getCategoria() != null) {
                criteria.add(Restrictions.eq("categoria", filtro.getCategoria()));
            }
        }
    }
}
