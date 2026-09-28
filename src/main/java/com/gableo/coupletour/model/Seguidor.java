package com.gableo.coupletour.model;

import jakarta.persistence.*;

@Entity
@Table(name = "seguidores", schema = "stage", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"relacionamento_id", "seguidor_id"})
})
public class Seguidor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "relacionamento_id", nullable = false)
    private Relacionamento relacionamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seguidor_id", nullable = false)
    private Usuario seguidor;

    public Seguidor() {}

    public Seguidor(Relacionamento relacionamento, Usuario seguidor) {
        this.relacionamento = relacionamento;
        this.seguidor = seguidor;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Relacionamento getRelacionamento() { return relacionamento; }
    public void setRelacionamento(Relacionamento relacionamento) { this.relacionamento = relacionamento; }
    public Usuario getSeguidor() { return seguidor; }
    public void setSeguidor(Usuario seguidor) { this.seguidor = seguidor; }
}
