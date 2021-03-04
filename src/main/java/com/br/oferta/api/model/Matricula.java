package com.br.oferta.api.model;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "matricula", schema = "evolucao")
public class Matricula implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "A descrição é obrigatória")
    @Column(name = "descricao")
    private String descricao;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "data_registro")
    private LocalDate dataRegistro;

    @Column(name = "status")
    private boolean status;

    @Column(name = "novo")
    private boolean novo;

    @NotNull(message = "A turma é obrigatório")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "turma_id", foreignKey = @ForeignKey(name = "fk_turma_matricula"))
    private Turma turma;

    @NotNull(message = "A aluno é obrigatório")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", foreignKey = @ForeignKey(name = "fk_usuario_matricula"))
    private Usuario usuario;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Matricula other = (Matricula) obj;
        if (getId() == null) {
            if (other.getId() != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

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

    public LocalDate getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDate dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isNovo() {
        return novo;
    }

    public void setNovo(boolean novo) {
        this.novo = novo;
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
