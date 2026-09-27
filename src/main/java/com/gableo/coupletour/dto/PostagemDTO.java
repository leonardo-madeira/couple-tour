package com.gableo.coupletour.dto;

import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;
import java.util.List;

public class PostagemDTO {

    private Long lugarId;
    private BigDecimal nota1;
    private BigDecimal nota2;
    private BigDecimal nota3;
    private BigDecimal nota4;
    private String descricao;
    private Boolean postVisibility = true;
    private List<MultipartFile> fotos;

    public Long getLugarId() { return lugarId; }
    public void setLugarId(Long lugarId) { this.lugarId = lugarId; }
    public BigDecimal getNota1() { return nota1; }
    public void setNota1(BigDecimal nota1) { this.nota1 = nota1; }
    public BigDecimal getNota2() { return nota2; }
    public void setNota2(BigDecimal nota2) { this.nota2 = nota2; }
    public BigDecimal getNota3() { return nota3; }
    public void setNota3(BigDecimal nota3) { this.nota3 = nota3; }
    public BigDecimal getNota4() { return nota4; }
    public void setNota4(BigDecimal nota4) { this.nota4 = nota4; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public Boolean getPostVisibility() { return postVisibility; }
    public void setPostVisibility(Boolean postVisibility) { this.postVisibility = postVisibility; }
    public List<MultipartFile> getFotos() { return fotos; }
    public void setFotos(List<MultipartFile> fotos) { this.fotos = fotos; }
}
