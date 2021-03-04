/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.oferta.api.util.filter;

import java.time.LocalDate;

/**
 *
 * @author PMBV-163979
 */
public class ContatoFilter {

    private Long id;
    private String nome;
    private String email;
    private String descricao;
    private LocalDate desde;
    private LocalDate ate;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDesde() {
        return desde;
    }

    public void setDesde(LocalDate desde) {
        this.desde = desde;
    }

    public LocalDate getAte() {
        return ate;
    }

    public void setAte(LocalDate ate) {
        this.ate = ate;
    }

}
