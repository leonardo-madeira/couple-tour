package com.gableo.coupletour.dto;

import org.springframework.web.multipart.MultipartFile;

public class CasalPageEditDTO {
    private MultipartFile fotoPerfil;
    private String resposta1;
    private String resposta2;
    private String resposta3;

    private String label1;
    private String label2;
    private String label3;

    public MultipartFile getFotoPerfil() { return fotoPerfil; }
    public void setFotoPerfil(MultipartFile fotoPerfil) { this.fotoPerfil = fotoPerfil; }
    public String getResposta1() { return resposta1; }
    public void setResposta1(String resposta1) { this.resposta1 = resposta1; }
    public String getResposta2() { return resposta2; }
    public void setResposta2(String resposta2) { this.resposta2 = resposta2; }
    public String getResposta3() { return resposta3; }
    public void setResposta3(String resposta3) { this.resposta3 = resposta3; }

    public String getLabel1() { return label1; }
    public void setLabel1(String label1) { this.label1 = label1; }
    public String getLabel2() { return label2; }
    public void setLabel2(String label2) { this.label2 = label2; }
    public String getLabel3() { return label3; }
    public void setLabel3(String label3) { this.label3 = label3; }
}
