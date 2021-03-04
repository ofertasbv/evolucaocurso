/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.oferta.api.util.filter;

import com.br.oferta.api.model.Turma;
import com.br.oferta.api.model.Usuario;
import java.time.LocalDate;

/**
 *
 * @author PMBV-163979
 */
public class MatriculaFilter {

    private Long id;
    private String descricao;
    private LocalDate desde;
    private LocalDate ate;
    private Turma turma;
    private Usuario usuario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}
