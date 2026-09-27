package com.gableo.coupletour.model;

import jakarta.persistence.*;

@Entity
@Table(name = "fotos_avaliacao_individual", schema = "stage")
public class FotoAvaliacaoIndividual {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avaliacao_id", nullable = false)
    private AvaliacaoIndividual avaliacao;

    @Column(nullable = false, length = 2048)
    private String url;

    public FotoAvaliacaoIndividual() {}

    public FotoAvaliacaoIndividual(AvaliacaoIndividual avaliacao, String url) {
        this.avaliacao = avaliacao;
        this.url = url;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public AvaliacaoIndividual getAvaliacao() { return avaliacao; }
    public void setAvaliacao(AvaliacaoIndividual avaliacao) { this.avaliacao = avaliacao; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
}
