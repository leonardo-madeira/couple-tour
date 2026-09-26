package com.gableo.coupletour.model;

import jakarta.persistence.*;

@Entity
@Table(name = "relacionamentos", schema = "stage")
public class Relacionamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_a_id", nullable = false)
    private Usuario usuarioA;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_b_id", nullable = false)
    private Usuario usuarioB;

    @Column(name = "relacionamento_ativo", nullable = false)
    private Boolean relacionamentoAtivo = true;

    public Relacionamento() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuarioA() { return usuarioA; }
    public void setUsuarioA(Usuario usuarioA) { this.usuarioA = usuarioA; }

    public Usuario getUsuarioB() { return usuarioB; }
    public void setUsuarioB(Usuario usuarioB) { this.usuarioB = usuarioB; }

    public Boolean getRelacionamentoAtivo() { return relacionamentoAtivo; }
    public void setRelacionamentoAtivo(Boolean relacionamentoAtivo) { this.relacionamentoAtivo = relacionamentoAtivo; }
}
