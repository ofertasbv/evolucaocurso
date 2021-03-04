package com.br.oferta.api.model;

import com.br.oferta.api.util.validation.AtributoConfirmacao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "usuario", schema = "evolucao")
@DynamicUpdate
@AtributoConfirmacao(atributo = "senha", atributoConfirmacao = "confirmaSenha", message = "Confirmação da senha não confere")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "E-mail inválido")
    @Column(name = "email")
    private String email;

    @NotBlank(message = "A senha é obrigatório")
    @Column(name = "senha")
    private String senha;

    @NotBlank(message = "Nome é obrigatório")
    @Column(name = "nome")
    private String nome;

    @NotBlank(message = "Cpf é obrigatório")
    @Column(name = "cpf")
    private String cpf;

    @NotBlank(message = "Telefone é obrigatório")
    @Column(name = "telefone")
    private String telefone;

    @Column(name = "foto")
    private String foto;

    @NotNull(message = "Data de nascimento é obrigatório")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "data_registro")
    private LocalDate dataRegistro;

    @Column(name = "status")
    private boolean status;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL, CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinTable(name = "usuario_grupo",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "grupo_id"),
            foreignKey = @ForeignKey(name = "fk_usuario_id"),
            inverseForeignKey = @ForeignKey(name = "fk_grupo_id"))
    private List<Grupo> grupos = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "endereco_usuario",
            joinColumns = @JoinColumn(name = "endereco_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id"),
            foreignKey = @ForeignKey(name = "fk_endereco_id"),
            inverseForeignKey = @ForeignKey(name = "fk_usuario_id"))
    private List<Endereco> enderecos = new ArrayList<>();

    @ManyToMany(mappedBy = "usuarios", fetch = FetchType.LAZY)
    private List<Turma> turmas;

    @Transient
    private String confirmaSenha;

    @PrePersist
    @PreUpdate
    private void preUpdate() {
        this.setConfirmaSenha(getSenha());
    }

    public boolean isNovo() {
        return getId() == null;
    }

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
        Usuario other = (Usuario) obj;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public LocalDate getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDate dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public List<Grupo> getGrupos() {
        return grupos;
    }

    public void setGrupos(List<Grupo> grupos) {
        this.grupos = grupos;
    }

    public String getConfirmaSenha() {
        return confirmaSenha;
    }

    public void setConfirmaSenha(String confirmaSenha) {
        this.confirmaSenha = confirmaSenha;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<Turma> getTurmas() {
        return turmas;
    }

    public void setTurmas(List<Turma> turmas) {
        this.turmas = turmas;
    }

    public List<Endereco> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<Endereco> enderecos) {
        this.enderecos = enderecos;
    }

}
