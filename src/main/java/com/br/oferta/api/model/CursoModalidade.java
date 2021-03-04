/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.oferta.api.model;

/**
 *
 * @author fabio
 */
public enum CursoModalidade {
    PRESENCIAL("PRESENCIAL"),
    SEMIPRESENCIAL("SEMIPRESENCIAL"),
    EAD("EAD");

    private final String descricao;

    CursoModalidade(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
