//package com.br.oferta.api.service;
//
//import com.br.oferta.api.model.Grupo;
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.EmptyResultDataAccessException;
//import org.springframework.stereotype.Service;
//
//import com.br.oferta.api.model.Pessoa;
//import com.br.oferta.api.model.Usuario;
//import java.util.List;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;
//import org.springframework.http.ResponseEntity;
//import com.br.oferta.api.repository.PessoaRepository;
//import com.br.oferta.api.repository.UsuarioRepository;
//import com.br.oferta.api.service.serviceImpl.PessoaServiceImpl;
//import com.br.oferta.api.util.exception.CpfCnpjClienteJaCadastradoException;
//import com.br.oferta.api.util.exception.EmailUsuarioJaCadastradoException;
//import com.br.oferta.api.util.exception.SenhaObrigatoriaUsuarioException;
//import com.br.oferta.api.util.filter.PessoaFilter;
//import com.br.oferta.api.util.paginacao.PaginacaoUtil;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.time.LocalDate;
//import org.hibernate.Criteria;
//import org.hibernate.Session;
//import org.hibernate.criterion.MatchMode;
//import org.hibernate.criterion.Projections;
//import org.hibernate.criterion.Restrictions;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.StringUtils;
//
//@Service
//public class PessoaService implements PessoaServiceImpl {
//
//    @Autowired
//    private PessoaRepository pessoaRepository;
//
//    @Autowired
//    private GrupoService grupoService;
//
//    @Autowired
//    private UsuarioRepository usuarioRepository;
//    
//    @Autowired
//    private PaginacaoUtil paginacaoUtil;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @PersistenceContext
//    private EntityManager em;
//
//    @Value("${contato.disco.raiz}")
//    private Path local;
//
//    private static final Logger logger = LoggerFactory.getLogger(PessoaService.class);
//
//    @Override
//    public long count() {
//        Query query = em.createQuery("SELECT COUNT(c) FROM Pessoa c");
//        return (long) query.getSingleResult();
//    }
//
//    @Override
//    public List<Pessoa> findBySort() {
//        Query query = em.createQuery("SELECT a FROM Pessoa a ORDER BY a.id DESC");
//        return query.getResultList();
//    }
//
//    @Override
//    public List<Pessoa> findByTurmaById(Long id) {
//        Query query = em.createQuery("SELECT p FROM Pessoa p INNER JOIN p.turmas t WHERE t.id =:id");
//        query.setParameter("id", id);
//        return query.getResultList();
//    }
//
//    @Override
//    public Optional<Pessoa> findById(Long id) {
//        return pessoaRepository.findById(id);
//    }
//
//    @Override
//    public List<Pessoa> findByNomeContaining(String nome) {
//        Query query = em.createQuery("SELECT a FROM Pessoa a WHERE a.nome =:nome");
//        return query.getResultList();
//    }
//
//    @Override
//    public Pessoa findByTelefone(String telefone) {
//        Query query = em.createQuery("SELECT p FROM Pessoa p WHERE p.telefone =:telefone");
//        query.setParameter("telefone", telefone);
//        return (Pessoa) query.getSingleResult();
//    }
//
//    @Override
//    public Pessoa create(Pessoa p) {
//        p.setDataRegistro(LocalDate.now());
//
//        Grupo grupo = grupoService.findById(3L).get();
//        p.getUsuario().getGrupos().clear();
//        p.getUsuario().getGrupos().add(0, grupo);
//        p.getUsuario().setSenha(passwordEncoder.encode(p.getUsuario().getSenha()));
//
//        Optional<Pessoa> pessoaExistente = pessoaRepository.findByCpfIgnoreCase(p.getCpf());
//        if (pessoaExistente.isPresent() && !pessoaExistente.get().equals(p)) {
//            throw new CpfCnpjClienteJaCadastradoException("Cpf da pessoa já cadastrado");
//        }
//
////        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(p.getUsuario().getEmail());
////        if (usuarioExistente.isPresent() && !usuarioExistente.get().equals(p.getUsuario().isNovo())) {
////            throw new EmailUsuarioJaCadastradoException("E-mail já cadastrado");
////        }
////
////        if (p.isNovo() && StringUtils.isEmpty(p.getUsuario().getSenha())) {
////            throw new SenhaObrigatoriaUsuarioException("Senha é obrigatória para novo usuário");
////        }
////
////        if (p.isNovo() || !StringUtils.isEmpty(p.getUsuario().getSenha())) {
////            p.getUsuario().setSenha(this.passwordEncoder.encode(p.getUsuario().getSenha()));
////        } else if (StringUtils.isEmpty(p.getUsuario().getSenha())) {
////            p.getUsuario().setSenha(usuarioExistente.get().getSenha());
////        }
////        p.getUsuario().setConfirmaSenha(p.getUsuario().getSenha());
//        System.out.println("Grupo:" + grupo.getId());
//        System.out.println("Email: " + p.getUsuario().getEmail());
//        System.out.println("Senha: " + p.getUsuario().getSenha());
//        System.out.println("DataRegistro: " + p.getDataRegistro());
//
//        return pessoaRepository.saveAndFlush(p);
//    }
//
//    @Override
//    public Pessoa update(Long id, Pessoa p) {
//        p.setDataRegistro(LocalDate.now());
//        return pessoaRepository.saveAndFlush(p);
//    }
//
//    @Override
//    public ResponseEntity delete(Long id) {
//        Pessoa exclui = pessoaRepository.findById(id).get();
//        if (exclui == null) {
//            throw new EmptyResultDataAccessException(1);
//        }
//        pessoaRepository.deleteById(id);
//        return ResponseEntity.ok(exclui);
//    }
//
//    @Override
//    public void excluirFoto(String foto) {
//        try {
//            Files.deleteIfExists(this.local.resolve(foto));
//        } catch (IOException e) {
//            logger.warn(String.format("Erro apagando foto '%s'. Mensagem: %s", foto, e.getMessage()));
//        }
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public Page<Pessoa> filtrar(PessoaFilter filtro, Pageable pageable) {
//        Criteria criteria = em.unwrap(Session.class).createCriteria(Pessoa.class);
//
//        paginacaoUtil.preparar(criteria, pageable);
//        adicionarFiltro(filtro, criteria);
//
//        return new PageImpl<>(criteria.list(), pageable, total(filtro));
//    }
//
//    private Long total(PessoaFilter filtro) {
//        Criteria criteria = em.unwrap(Session.class).createCriteria(Pessoa.class);
//        adicionarFiltro(filtro, criteria);
//        criteria.setProjection(Projections.rowCount());
//        return (Long) criteria.uniqueResult();
//    }
//
//    private void adicionarFiltro(PessoaFilter filtro, Criteria criteria) {
//        if (filtro != null) {
//
//            if (!StringUtils.isEmpty(filtro.getNome())) {
//                criteria.add(Restrictions.ilike("nome", filtro.getNome().toLowerCase(), MatchMode.ANYWHERE));
//            }
//
//            if (!StringUtils.isEmpty(filtro.getCpf())) {
//                criteria.add(Restrictions.ilike("cpf", filtro.getCpf(), MatchMode.ANYWHERE));
//            }
//        }
//    }
//}
