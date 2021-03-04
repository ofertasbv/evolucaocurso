/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.oferta.api.util.filter;

import com.br.oferta.api.model.Categoria;
import com.br.oferta.api.model.CursoModalidade;

/**
 *
 * @author PMBV-163979
 */
public class CursoFilter {

    private Long id;
    private String nome;
    private CursoModalidade modalidade;
    private Categoria categoria;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public CursoModalidade getModalidade() {
        return modalidade;
    }

    public void setModalidade(CursoModalidade modalidade) {
        this.modalidade = modalidade;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

}
