package com.br.oferta.api.service.serviceImpl;

import com.br.oferta.api.model.Curso;
import com.br.oferta.api.util.filter.CursoFilter;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author fabio
 */
public interface CursoServiceImpl {

    List<Curso> findAll();

    Optional<Curso> findById(Long id);

    Curso create(Curso p);

    ResponseEntity update(Long id, Curso p);

    ResponseEntity delete(Long id);

    List<Curso> findByNome(String nome);

    List<Curso> findByDestaque(boolean destaque);

    Page<Curso> findAllByPage(Pageable pageable);

    Page<Curso> filtrar(CursoFilter filtro, Pageable pageable);

    List<Curso> findBySubCategoriaById(Long id);

    List<Curso> findByPessoaById(Long id);

    void excluirFoto(String foto);

    long count();

}
