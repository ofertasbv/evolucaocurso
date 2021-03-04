package com.br.oferta.api.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

@Entity
@Table(name = "curso", schema = "evolucao")
public class Curso implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    @Column(name = "nome")
    private String nome;

    @NotBlank(message = "A descrição é obrigatória")
    @Column(name = "descricao", columnDefinition = "text")
    private String descricao;

//    @NumberFormat(pattern =  "#.###,##")
    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.50", message = "O valor do curso deve ser maior que R$0,50")
    @DecimalMax(value = "9999999.99", message = "O valor do curso deve ser menor que R$9.999.999,99")
    @Column(name = "valor")
    private BigDecimal valor;

    @NotNull(message = "Valor de Desconto é obrigatório")
    @DecimalMin(value = "0.50", message = "O Valor de Desconto do curso deve ser maior que R$0,50")
    @DecimalMax(value = "9999999.99", message = "O Valor Desconto do curso deve ser menor que R$9.999.999,99")
    @Column(name = "desconto")
    private BigDecimal desconto;

    @NotNull(message = "Valor Total é obrigatório")
    @DecimalMin(value = "0.50", message = "O Valor Total do curso deve ser maior que R$0,50")
    @DecimalMax(value = "9999999.99", message = "O Valor Total do curso deve ser menor que R$9.999.999,99")
    @Column(name = "valor_total")
    private BigDecimal valorTotal;

    @NotBlank(message = "A foto é obrigatório")
    @Column(name = "foto")
    private String foto;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "data_registro")
    private LocalDate dataRegistro;

    @OneToOne(cascade = {CascadeType.ALL, CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ementa_id", foreignKey = @ForeignKey(name = "fk_curso_ementa"))
    private Ementa ementa = new Ementa();

    @NotNull(message = "A modalidade é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(name = "modalidade", nullable = false)
    private CursoModalidade modalidade = CursoModalidade.PRESENCIAL;

    @NotNull(message = "A categoria é obrigatório")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_id", foreignKey = @ForeignKey(name = "fk_curso_categoria"))
    private Categoria categoria;

    @Column(name = "status")
    private boolean status;

    @Column(name = "novo")
    private boolean novo;

    @Column(name = "destaque")
    private boolean destaque;

    public boolean isNova() {
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
        Curso other = (Curso) obj;
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

    public Ementa getEmenta() {
        return ementa;
    }

    public void setEmenta(Ementa ementa) {
        this.ementa = ementa;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getDesconto() {
        return desconto;
    }

    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
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

    public boolean isDestaque() {
        return destaque;
    }

    public void setDestaque(boolean destaque) {
        this.destaque = destaque;
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
