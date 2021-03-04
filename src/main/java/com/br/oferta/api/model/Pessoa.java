//package com.br.oferta.api.model;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import java.io.Serializable;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//import javax.persistence.CascadeType;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.ForeignKey;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.JoinTable;
//import javax.persistence.ManyToMany;
//import javax.persistence.OneToOne;
//import javax.persistence.Table;
//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.NotNull;
//import org.springframework.format.annotation.DateTimeFormat;
//
//@Entity
//@Table(name = "pessoa", schema = "evolucao")
//public class Pessoa implements Serializable {
//
//    private static final long serialVersionUID = 1L;
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
//    private Long id;
//
//    @NotBlank(message = "Nome é obrigatório")
//    @Column(name = "nome")
//    private String nome;
//
//    @NotBlank(message = "Cpf é obrigatório")
//    @Column(name = "cpf")
//    private String cpf;
//
//    @NotBlank(message = "Telefone é obrigatório")
//    @Column(name = "telefone")
//    private String telefone;
//
//    @Column(name = "foto")
//    private String foto;
//
//    @NotNull(message = "Data de nascimento é obrigatório")
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    @Column(name = "data_nascimento")
//    private LocalDate dataNascimento;
//
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    @Column(name = "data_registro")
//    private LocalDate dataRegistro;
//
//    @Column(name = "ativo")
//    private boolean ativo;
//
//    @NotNull(message = "O usuário é obrigatório")
//    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "usuario_id", foreignKey = @ForeignKey(name = "fk_pessoa_usuario"))
//    private Usuario usuario = new Usuario();
//
//    @JsonIgnore
//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "endereco_pessoa",
//            joinColumns = @JoinColumn(name = "pessoa_id"),
//            inverseJoinColumns = @JoinColumn(name = "endereco_id"),
//            foreignKey = @ForeignKey(name = "fk_pessoa_id"),
//            inverseForeignKey = @ForeignKey(name = "fk_endereco_id"))
//    private List<Endereco> enderecos;// = new ArrayList<>();
//
//    @ManyToMany(mappedBy = "pessoas", fetch = FetchType.EAGER)
//    private List<Turma> turmas;
//
//    public boolean isNovo() {
//        return id == null;
//    }
//
//    @Override
//    public int hashCode() {
//        final int prime = 31;
//        int result = 1;
//        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
//        return result;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) {
//            return true;
//        }
//        if (obj == null) {
//            return false;
//        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
//        Pessoa other = (Pessoa) obj;
//        if (getId() == null) {
//            if (other.getId() != null) {
//                return false;
//            }
//        } else if (!id.equals(other.id)) {
//            return false;
//        }
//        return true;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getNome() {
//        return nome;
//    }
//
//    public void setNome(String nome) {
//        this.nome = nome;
//    }
//
//    public String getCpf() {
//        return cpf;
//    }
//
//    public void setCpf(String cpf) {
//        this.cpf = cpf;
//    }
//
//    public String getTelefone() {
//        return telefone;
//    }
//
//    public void setTelefone(String telefone) {
//        this.telefone = telefone;
//    }
//
//    public String getFoto() {
//        return foto;
//    }
//
//    public void setFoto(String foto) {
//        this.foto = foto;
//    }
//
//    public LocalDate getDataNascimento() {
//        return dataNascimento;
//    }
//
//    public void setDataNascimento(LocalDate dataNascimento) {
//        this.dataNascimento = dataNascimento;
//    }
//
//    public LocalDate getDataRegistro() {
//        return dataRegistro;
//    }
//
//    public void setDataRegistro(LocalDate dataRegistro) {
//        this.dataRegistro = dataRegistro;
//    }
//
//    public boolean isAtivo() {
//        return ativo;
//    }
//
//    public void setAtivo(boolean ativo) {
//        this.ativo = ativo;
//    }
//
//    public Usuario getUsuario() {
//        return usuario;
//    }
//
//    public void setUsuario(Usuario usuario) {
//        this.usuario = usuario;
//    }
//
//    public List<Endereco> getEnderecos() {
//        return enderecos;
//    }
//
//    public void setEnderecos(List<Endereco> enderecos) {
//        this.enderecos = enderecos;
//    }
//
//    public List<Turma> getTurmas() {
//        return turmas;
//    }
//
//    public void setTurmas(List<Turma> turmas) {
//        this.turmas = turmas;
//    }
//
//}
