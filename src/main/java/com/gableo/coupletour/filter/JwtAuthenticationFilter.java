package com.gableo.coupletour.filter;

import com.gableo.coupletour.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.startsWith("/login") || 
               uri.startsWith("/cadastro") || 
               uri.startsWith("/css") || 
               uri.startsWith("/js") || 
               uri.startsWith("/img") ||
               uri.equals("/favicon.ico");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = extrairToken(request);

        if (token == null) {
            response.sendRedirect("/login");
            return;
        }

        try {
            io.jsonwebtoken.Claims claims = jwtService.validarETerClaims(token);
            request.setAttribute("usuarioId", claims.getSubject());
            request.setAttribute("usuarioNome", claims.get("nome"));
            request.setAttribute("usuarioEmail", claims.get("email"));
            request.setAttribute("usuarioPronome", claims.get("pronome"));
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            response.sendRedirect("/login");
        }
    }

    private String extrairToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("JWT-TOKEN".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        
        return null;
    }
}
