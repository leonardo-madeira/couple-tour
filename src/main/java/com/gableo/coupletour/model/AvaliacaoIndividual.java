package com.gableo.coupletour.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "avaliacao_individual", schema = "stage")
public class AvaliacaoIndividual {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lugar_id", nullable = false)
    private Lugar lugar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "relacionamento_id", nullable = false)
    private Relacionamento relacionamento;

    @Column(name = "nota_pergunta_1", nullable = false, precision = 3, scale = 1)
    private BigDecimal notaPergunta1;

    @Column(name = "nota_pergunta_2", nullable = false, precision = 3, scale = 1)
    private BigDecimal notaPergunta2;

    @Column(name = "nota_pergunta_3", nullable = false, precision = 3, scale = 1)
    private BigDecimal notaPergunta3;

    @Column(name = "nota_pergunta_4", nullable = false, precision = 3, scale = 1)
    private BigDecimal notaPergunta4;

    @Column(name = "nota_media", insertable = false, updatable = false)
    private BigDecimal notaMedia;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    @Column(name = "post_visibility", nullable = false)
    private Boolean postVisibility = true;

    public AvaliacaoIndividual() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Lugar getLugar() { return lugar; }
    public void setLugar(Lugar lugar) { this.lugar = lugar; }
    public Relacionamento getRelacionamento() { return relacionamento; }
    public void setRelacionamento(Relacionamento relacionamento) { this.relacionamento = relacionamento; }
    public BigDecimal getNotaPergunta1() { return notaPergunta1; }
    public void setNotaPergunta1(BigDecimal notaPergunta1) { this.notaPergunta1 = notaPergunta1; }
    public BigDecimal getNotaPergunta2() { return notaPergunta2; }
    public void setNotaPergunta2(BigDecimal notaPergunta2) { this.notaPergunta2 = notaPergunta2; }
    public BigDecimal getNotaPergunta3() { return notaPergunta3; }
    public void setNotaPergunta3(BigDecimal notaPergunta3) { this.notaPergunta3 = notaPergunta3; }
    public BigDecimal getNotaPergunta4() { return notaPergunta4; }
    public void setNotaPergunta4(BigDecimal notaPergunta4) { this.notaPergunta4 = notaPergunta4; }
    public BigDecimal getNotaMedia() { return notaMedia; }
    public void setNotaMedia(BigDecimal notaMedia) { this.notaMedia = notaMedia; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public Boolean getPostVisibility() { return postVisibility; }
    public void setPostVisibility(Boolean postVisibility) { this.postVisibility = postVisibility; }
}
