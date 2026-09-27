package com.gableo.coupletour.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret:MinhaChaveSecretaMuitoSeguraParaOCoupleTourComPeloMenos256BitsDeTamanho}")
    private String secret;

    @Value("${jwt.expiration:86400000}") // 24 horas
    private long expirationMs;

    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String gerarToken(com.gableo.coupletour.model.Usuario usuario) {
        Date agora = new Date();
        Date dataExpiracao = new Date(agora.getTime() + expirationMs);

        return Jwts.builder()
                .subject(usuario.getPublicId())
                .claim("public_id", usuario.getPublicId())
                .claim("nome", usuario.getNome())
                .claim("email", usuario.getEmail())
                .claim("data_nascimento", usuario.getDataNascimento().toString())
                .claim("pronome", usuario.getPronome() != null ? usuario.getPronome().getId() : null)
                .claim("unique_token", usuario.getUniqueToken())
                .issuedAt(agora)
                .expiration(dataExpiracao)
                .signWith(getSigningKey())
                .compact();
    }

    public Claims validarETerClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extrairPublicId(String token) {
        return validarETerClaims(token).getSubject();
    }
}
