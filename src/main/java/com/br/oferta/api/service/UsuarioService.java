/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.oferta.api.service;

import com.br.oferta.api.service.serviceImpl.UsuarioServiceImpl;
import com.br.oferta.api.model.Usuario;
import com.br.oferta.api.repository.UsuarioRepository;
import com.br.oferta.api.util.error.ServiceNotFoundExeception;
import com.br.oferta.api.util.exception.EmailUsuarioJaCadastradoException;
import com.br.oferta.api.util.exception.SenhaObrigatoriaUsuarioException;
import com.br.oferta.api.util.filter.UsuarioFilter;
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
import org.hibernate.sql.JoinType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 *
 * @author fabio
 */
@Service
public class UsuarioService implements UsuarioServiceImpl {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PaginacaoUtil paginacaoUtil;

    @PersistenceContext
    private EntityManager em;

    @Override
    public long count() {
        Query query = em.createQuery("SELECT COUNT(c) FROM Usuario c");
        return (long) query.getSingleResult();
    }

    @Override
    public List<Usuario> findBySort() {
        Query query = em.createQuery("SELECT a FROM Usuario a ORDER BY a.id ASC");
        return query.getResultList();
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Usuario create(Usuario usuario) {
        if (usuario.getId() == null) {
            Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuario.getEmail());
            if (usuarioExistente.isPresent() && !usuarioExistente.get().equals(usuario)) {
                throw new EmailUsuarioJaCadastradoException("E-mail já cadastrado");
            }

            if (usuario.isNovo() && StringUtils.isEmpty(usuario.getSenha())) {
                throw new SenhaObrigatoriaUsuarioException("Senha é obrigatória para novo usuário");
            }

            if (usuario.isNovo() || !StringUtils.isEmpty(usuario.getSenha())) {
                usuario.setSenha(this.passwordEncoder.encode(usuario.getSenha()));
            } else if (StringUtils.isEmpty(usuario.getSenha())) {
                usuario.setSenha(usuarioExistente.get().getSenha());
            }
            usuario.setConfirmaSenha(usuario.getSenha());

            return usuarioRepository.save(usuario);
        }
        return null;

    }

    @Override
    public ResponseEntity update(Long id, Usuario u) {
        Usuario salva = usuarioRepository.findById(id).get();
        if (salva == null) {
            throw new ServiceNotFoundExeception("Usuário não encotrado com ID: " + id);
        }
        BeanUtils.copyProperties(u, salva, "id");
        usuarioRepository.save(salva);
        return ResponseEntity.ok(salva);
    }

    @Override
    public ResponseEntity delete(Long id) {
        Usuario exclui = usuarioRepository.findById(id).get();
        if (exclui == null) {
            throw new EmptyResultDataAccessException(1);
        }

        usuarioRepository.deleteById(id);
        return ResponseEntity.ok(exclui);
    }

    @Override
    public List<String> permissoes(Usuario usuario) {
        return em.createQuery(
                "select distinct p.descricao from Usuario u inner join u.grupos g inner join g.permissoes p where u = :usuario", String.class
        )
                .setParameter("usuario", usuario)
                .getResultList();
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return em
                .createQuery("from Usuario where lower(email) = lower(:email)", Usuario.class
                )
                .setParameter("email", email).getResultList().stream().findFirst();

    }

    @Transactional(readOnly = true)
    @Override
    public Usuario buscarComGrupos(Long id) {
        Criteria criteria = em.unwrap(Session.class
        ).createCriteria(Usuario.class
        );
        criteria.createAlias("grupos", "g", JoinType.LEFT_OUTER_JOIN);
        criteria.add(Restrictions.eq("id", id));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return (Usuario) criteria.uniqueResult();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Usuario> filtrar(UsuarioFilter filtro, Pageable pageable) {
        Criteria criteria = em.unwrap(Session.class).createCriteria(Usuario.class);

        paginacaoUtil.preparar(criteria, pageable);
        adicionarFiltro(filtro, criteria);

        return new PageImpl<>(criteria.list(), pageable, total(filtro));
    }

    private Long total(UsuarioFilter filtro) {
        Criteria criteria = em.unwrap(Session.class).createCriteria(Usuario.class);
        adicionarFiltro(filtro, criteria);
        criteria.setProjection(Projections.rowCount());
        return (Long) criteria.uniqueResult();
    }

    private void adicionarFiltro(UsuarioFilter filtro, Criteria criteria) {
        if (filtro != null) {

            if (!StringUtils.isEmpty(filtro.getNome())) {
                criteria.add(Restrictions.ilike("nome", filtro.getNome().toLowerCase(), MatchMode.ANYWHERE));
            }

            if (!StringUtils.isEmpty(filtro.getCpf())) {
                criteria.add(Restrictions.ilike("cpf", filtro.getCpf(), MatchMode.ANYWHERE));
            }
        }
    }

    @Override
    public Optional<Usuario> findByCpf(String cpf) {
        Query query = em.createQuery("SELECT u FROM Usuario u WHERE u.cpf =:cpf");
        query.setParameter("cpf", cpf);
        return (Optional<Usuario>) query.getSingleResult();
    }

}
