/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.oferta.api.service;

import com.br.oferta.api.service.serviceImpl.CategoriaServiceImpl;
import com.br.oferta.api.model.Categoria;
import com.br.oferta.api.repository.CategoriaRepository;
import com.br.oferta.api.util.exception.NomeCategoriaJaCadastradoException;
import com.br.oferta.api.util.filter.CategoriaFilter;
import com.br.oferta.api.util.paginacao.PaginacaoUtil;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
public class CategoriaService implements CategoriaServiceImpl {
    
    private final CategoriaRepository categoriaRepository;
    
    @PersistenceContext
    private final EntityManager em;
    
    private final PaginacaoUtil paginacaoUtil;

//    @Value("${contato.disco.raiz}")
    private Path local;
    
    private static final Logger logger = LoggerFactory.getLogger(CategoriaService.class);
    
    @Autowired
    public CategoriaService(CategoriaRepository categoriaRepository, PaginacaoUtil paginacaoUtil, EntityManager em) {
        this.categoriaRepository = categoriaRepository;
        this.paginacaoUtil = paginacaoUtil;
        this.em = em;
    }
    
    @Override
    public long count() {
        Query query = em.createQuery("SELECT COUNT(c) FROM Categoria c");
        return (long) query.getSingleResult();
    }
    
    @Override
    public List<Categoria> findAll() {
        Query query = em.createQuery("SELECT c FROM Categoria c");
        return query.getResultList();
    }
    
    @Override
    public List<Categoria> findBySort() {
        Query query = em.createQuery("SELECT c FROM Categoria c ORDER BY c.id").setMaxResults(8);
        return query.getResultList();
    }
    
    @Override
    public Optional<Categoria> findById(Long id) {
        return categoriaRepository.findById(id);
    }
    
    @Override
    public List<Categoria> findAllByNome(String nome) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Categoria> query = criteriaBuilder.createQuery(Categoria.class);
        Root<Categoria> n = query.from(Categoria.class);
        
        javax.persistence.criteria.Path<String> nomePath = n.<String>get("nome");
        List<Predicate> predicates = new ArrayList<>();
        
        if (nome != null) {
            Predicate paramentro = criteriaBuilder.like(criteriaBuilder.lower(nomePath), "%" + nome.toLowerCase() + "%");
            predicates.add(paramentro);
        }
        
        query.where((Predicate[]) predicates.toArray(new Predicate[0]));
        query.orderBy(criteriaBuilder.desc(n.get("id")));
        TypedQuery<Categoria> typedQuery = em.createQuery(query);
        
        return typedQuery.getResultList();
    }
    
    @Override
    public Categoria create(Categoria c) {
        Optional<Categoria> optional = categoriaRepository.findByNomeIgnoreCase(c.getNome());
        if (optional.isPresent()) {
            throw new NomeCategoriaJaCadastradoException("Nome da categoria já cadastrado");
        }
        return categoriaRepository.save(c);
    }
    
    @Override
    public Categoria update(Long id, Categoria c) {
        return categoriaRepository.save(c);
    }
    
    @Override
    public ResponseEntity delete(Long id) {
        Optional<Categoria> existe = categoriaRepository.findById(id);
        if (existe.isPresent()) {
            throw new EmptyResultDataAccessException(1);
        }
        
        categoriaRepository.deleteById(id);
        return ResponseEntity.ok(existe);
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
    @Transactional(readOnly = true)
    public Page<Categoria> filtrar(CategoriaFilter filtro, Pageable pageable) {
        Criteria criteria = em.unwrap(Session.class).createCriteria(Categoria.class);
        
        paginacaoUtil.preparar(criteria, pageable);
        adicionarFiltro(filtro, criteria);
        
        return new PageImpl<>(criteria.list(), pageable, total(filtro));
    }
    
    private Long total(CategoriaFilter filtro) {
        Criteria criteria = em.unwrap(Session.class).createCriteria(Categoria.class);
        adicionarFiltro(filtro, criteria);
        criteria.setProjection(Projections.rowCount());
        return (Long) criteria.uniqueResult();
    }
    
    private void adicionarFiltro(CategoriaFilter filtro, Criteria criteria) {
        if (filtro != null) {
            
            if (!StringUtils.isEmpty(filtro.getNome())) {
                criteria.add(Restrictions.ilike("nome", filtro.getNome().toLowerCase(), MatchMode.ANYWHERE));
            }
        }
    }
}
