package com.gableo.coupletour.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface FeedProjection {
    Long getRelacionamentoId();
    Long getLugarId();
    String getLugarNome();
    String getCategoriaNome();
    BigDecimal getNotaMediaCasal();
    
    String getUsuarioAPublicId();
    String getUsuarioANome();
    BigDecimal getNotaMediaA();
    BigDecimal getANota1();
    BigDecimal getANota2();
    BigDecimal getANota3();
    BigDecimal getANota4();
    String getDescA();
    
    String getUsuarioBPublicId();
    String getUsuarioBNome();
    BigDecimal getNotaMediaB();
    BigDecimal getBNota1();
    BigDecimal getBNota2();
    BigDecimal getBNota3();
    BigDecimal getBNota4();
    String getDescB();
    
    String getFotosUrls();
    String getDataPostagem();
}
