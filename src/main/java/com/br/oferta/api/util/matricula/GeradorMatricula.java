/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.oferta.api.util.matricula;

import com.br.oferta.api.model.Curso;
import com.br.oferta.api.model.Turma;
import com.br.oferta.api.model.Usuario;

/**
 *
 * @author fabio
 */
public class GeradorMatricula {

    public String gerarMatricula(Turma turma, Usuario usuario) {
        String cpf = usuario.getCpf().substring(0, 3);
        String nomeTurma = turma.getNome().substring(0, 2);
        return cpf + nomeTurma;
    }

    public String gerarTurma(Curso curso) {
        String nomeTurma = curso.getNome().substring(0, 2);
        return nomeTurma;
    }

}
