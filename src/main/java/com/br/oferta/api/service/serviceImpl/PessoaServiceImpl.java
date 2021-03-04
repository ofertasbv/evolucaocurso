///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.br.oferta.api.service.serviceImpl;
//
//import com.br.oferta.api.model.Pessoa;
//import com.br.oferta.api.util.filter.PessoaFilter;
//import java.util.List;
//import java.util.Optional;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.ResponseEntity;
//
///**
// *
// * @author fabio
// */
//public interface PessoaServiceImpl {
//
//    List<Pessoa> findBySort();
//
//    Optional<Pessoa> findById(Long id);
//
//    Pessoa findByTelefone(String telefone);
//
//    List<Pessoa> findByTurmaById(Long id);
//
//    List<Pessoa> findByNomeContaining(String nome);
//
//    Page<Pessoa> filtrar(PessoaFilter filtro, Pageable pageable);
//
//    Pessoa create(Pessoa p);
//
//    Pessoa update(Long id, Pessoa p);
//
//    ResponseEntity delete(Long id);
//
//    void excluirFoto(String foto);
//
//    long count();
//}
