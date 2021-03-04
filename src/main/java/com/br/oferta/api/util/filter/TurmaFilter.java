/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.oferta.api.util.filter;

import com.br.oferta.api.model.Curso;
import com.br.oferta.api.model.TurmaTurno;
import java.time.LocalDate;

/**
 *
 * @author PMBV-163979
 */
public class TurmaFilter {

    private Long id;
    private String descricao;
    private TurmaTurno turno;
    private LocalDate desde;
    private LocalDate ate;
    private Curso Curso;

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

    public Curso getCurso() {
        return Curso;
    }

    public void setCurso(Curso Curso) {
        this.Curso = Curso;
    }

    public TurmaTurno getTurno() {
        return turno;
    }

    public void setTurno(TurmaTurno turno) {
        this.turno = turno;
    }

}
