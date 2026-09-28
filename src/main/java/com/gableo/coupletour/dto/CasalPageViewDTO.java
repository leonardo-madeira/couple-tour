package com.gableo.coupletour.dto;

import java.util.List;
import com.gableo.coupletour.dto.FeedProjection;
import com.gableo.coupletour.dto.CasalSeguidoProjection;

public class CasalPageViewDTO {

    private String nomeA;
    private String nomeB;

    private String fotoAUrl;
    private String fotoBUrl;

    private String label1ParaB;
    private String label2ParaB;
    private String label3ParaB;
    private String respostaA1;
    private String respostaA2;
    private String respostaA3;

    private String label1ParaA;
    private String label2ParaA;
    private String label3ParaA;
    private String respostaB1;
    private String respostaB2;
    private String respostaB3;

    private boolean podeEditarA;
    private boolean podeEditarB;
    private boolean seguindo;
    private int contagemSeguidores;
    
    public int getContagemSeguidores() { return contagemSeguidores; }
    public void setContagemSeguidores(int contagemSeguidores) { this.contagemSeguidores = contagemSeguidores; }
    
    
    public boolean isSeguindo() { return seguindo; }
    public void setSeguindo(boolean seguindo) { this.seguindo = seguindo; }


    public boolean isPodeEditarA() { return podeEditarA; }
    public void setPodeEditarA(boolean podeEditarA) { this.podeEditarA = podeEditarA; }
    public boolean isPodeEditarB() { return podeEditarB; }
    public void setPodeEditarB(boolean podeEditarB) { this.podeEditarB = podeEditarB; }

    private List<FeedProjection> feedCasal;
    private List<CasalSeguidoProjection> casaisSeguidos;

    public List<CasalSeguidoProjection> getCasaisSeguidos() { return casaisSeguidos; }
    public void setCasaisSeguidos(List<CasalSeguidoProjection> casaisSeguidos) { this.casaisSeguidos = casaisSeguidos; }

    public String getNomeA() { return nomeA; }
    public void setNomeA(String nomeA) { this.nomeA = nomeA; }
    public String getNomeB() { return nomeB; }
    public void setNomeB(String nomeB) { this.nomeB = nomeB; }

    public String getFotoAUrl() { return fotoAUrl; }
    public void setFotoAUrl(String fotoAUrl) { this.fotoAUrl = fotoAUrl; }
    public String getFotoBUrl() { return fotoBUrl; }
    public void setFotoBUrl(String fotoBUrl) { this.fotoBUrl = fotoBUrl; }

    public String getLabel1ParaB() { return label1ParaB; }
    public void setLabel1ParaB(String label1ParaB) { this.label1ParaB = label1ParaB; }
    public String getLabel2ParaB() { return label2ParaB; }
    public void setLabel2ParaB(String label2ParaB) { this.label2ParaB = label2ParaB; }
    public String getLabel3ParaB() { return label3ParaB; }
    public void setLabel3ParaB(String label3ParaB) { this.label3ParaB = label3ParaB; }

    public String getRespostaA1() { return respostaA1; }
    public void setRespostaA1(String respostaA1) { this.respostaA1 = respostaA1; }
    public String getRespostaA2() { return respostaA2; }
    public void setRespostaA2(String respostaA2) { this.respostaA2 = respostaA2; }
    public String getRespostaA3() { return respostaA3; }
    public void setRespostaA3(String respostaA3) { this.respostaA3 = respostaA3; }

    public String getLabel1ParaA() { return label1ParaA; }
    public void setLabel1ParaA(String label1ParaA) { this.label1ParaA = label1ParaA; }
    public String getLabel2ParaA() { return label2ParaA; }
    public void setLabel2ParaA(String label2ParaA) { this.label2ParaA = label2ParaA; }
    public String getLabel3ParaA() { return label3ParaA; }
    public void setLabel3ParaA(String label3ParaA) { this.label3ParaA = label3ParaA; }

    public String getRespostaB1() { return respostaB1; }
    public void setRespostaB1(String respostaB1) { this.respostaB1 = respostaB1; }
    public String getRespostaB2() { return respostaB2; }
    public void setRespostaB2(String respostaB2) { this.respostaB2 = respostaB2; }
    public String getRespostaB3() { return respostaB3; }
    public void setRespostaB3(String respostaB3) { this.respostaB3 = respostaB3; }


    public List<FeedProjection> getFeedCasal() { return feedCasal; }
    public void setFeedCasal(List<FeedProjection> feedCasal) { this.feedCasal = feedCasal; }
}
