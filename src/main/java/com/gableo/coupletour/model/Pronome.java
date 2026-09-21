package com.gableo.coupletour.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pronome", schema = "stage")
public class Pronome {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "terceira_pessoa", nullable = false)
    private String terceiraPessoa;

    @Column(name = "posse", nullable = false)
    private String posse;

    public Pronome() {}

    public Pronome(Integer id, String terceiraPessoa, String posse) {
        this.id = id;
        this.terceiraPessoa = terceiraPessoa;
        this.posse = posse;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTerceiraPessoa() {
        return terceiraPessoa;
    }

    public void setTerceiraPessoa(String terceiraPessoa) {
        this.terceiraPessoa = terceiraPessoa;
    }

    public String getPosse() {
        return posse;
    }

    public void setPosse(String posse) {
        this.posse = posse;
    }

    public String getTextoExibicao() {
        String terceira_upper = terceiraPessoa.substring(0, 1).toUpperCase() + terceiraPessoa.substring(1);
        String posse_upper = posse.substring(0, 1).toUpperCase() + posse.substring(1);
        return terceira_upper + "/" + posse_upper;
    }
}
