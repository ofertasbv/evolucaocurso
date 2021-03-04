package com.br.oferta.api.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "turma", schema = "evolucao")
public class Turma implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    @Column(name = "nome")
    private String nome;

    @NotBlank(message = "A descrição é obrigatória")
    @Column(name = "descricao")
    private String descricao;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "data_registro")
    private LocalDate dataRegistro;

    @NotNull(message = "A data de início é obrigatória")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "data_inicio")
    private LocalDate dataInicio;

    @NotNull(message = "A data de encerramento é obrigatória")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "data_encerramento")
    private LocalDate dataEncerramento;

    @Column(name = "novo")
    private boolean novo;

    @NotNull(message = "A quantidade é obrigatória")
    @Column(name = "quantidade")
    private Integer quantidade;

    @NotNull(message = "A turno é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(name = "turno", nullable = false)
    private TurmaTurno turno = TurmaTurno.DIURNO;

    @NotNull(message = "A status é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TurmaStatus status = TurmaStatus.ABERTA;

    @NotNull(message = "O curso é obrigatório")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "curso_id", foreignKey = @ForeignKey(name = "fk_curso_turma"))
    private Curso curso;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL, CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinTable(name = "turma_usuario",
            joinColumns = @JoinColumn(name = "turma_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id"),
            foreignKey = @ForeignKey(name = "fk_turma_id"),
            inverseForeignKey = @ForeignKey(name = "fk_usuario_id"))
    private List<Usuario> usuarios = new ArrayList<>();

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
        Turma other = (Turma) obj;
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataEncerramento() {
        return dataEncerramento;
    }

    public void setDataEncerramento(LocalDate dataEncerramento) {
        this.dataEncerramento = dataEncerramento;
    }

    public boolean isNovo() {
        return novo;
    }

    public void setNovo(boolean novo) {
        this.novo = novo;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public TurmaTurno getTurno() {
        return turno;
    }

    public void setTurno(TurmaTurno turno) {
        this.turno = turno;
    }

    public TurmaStatus getStatus() {
        return status;
    }

    public void setStatus(TurmaStatus status) {
        this.status = status;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

}
