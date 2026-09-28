package com.gableo.coupletour.model;

import jakarta.persistence.*;

@Entity
@Table(name = "casal_page_respostas", schema = "stage", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"relacionamento_id", "usuario_id"})
})
public class CasalPageRespostas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "relacionamento_id", nullable = false)
    private Relacionamento relacionamento;

    @Column(name = "foto_perfil_url", length = 500)
    private String fotoPerfilUrl;

    @Column(name = "resposta_1", columnDefinition = "TEXT")
    private String resposta1;

    @Column(name = "resposta_2", columnDefinition = "TEXT")
    private String resposta2;

    @Column(name = "resposta_3", columnDefinition = "TEXT")
    private String resposta3;

    public CasalPageRespostas() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Relacionamento getRelacionamento() { return relacionamento; }
    public void setRelacionamento(Relacionamento relacionamento) { this.relacionamento = relacionamento; }
    public String getFotoPerfilUrl() { return fotoPerfilUrl; }
    public void setFotoPerfilUrl(String fotoPerfilUrl) { this.fotoPerfilUrl = fotoPerfilUrl; }
    public String getResposta1() { return resposta1; }
    public void setResposta1(String resposta1) { this.resposta1 = resposta1; }
    public String getResposta2() { return resposta2; }
    public void setResposta2(String resposta2) { this.resposta2 = resposta2; }
    public String getResposta3() { return resposta3; }
    public void setResposta3(String resposta3) { this.resposta3 = resposta3; }
}
