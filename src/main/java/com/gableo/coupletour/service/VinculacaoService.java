package com.gableo.coupletour.service;

import com.gableo.coupletour.model.Relacionamento;
import com.gableo.coupletour.model.Usuario;
import com.gableo.coupletour.repository.RelacionamentoRepository;
import com.gableo.coupletour.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VinculacaoService {

    private final UsuarioRepository usuarioRepository;
    private final RelacionamentoRepository relacionamentoRepository;

    public VinculacaoService(UsuarioRepository usuarioRepository, RelacionamentoRepository relacionamentoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.relacionamentoRepository = relacionamentoRepository;
    }

    @Transactional
    public void vincular(String userPublicId, String tokenParceiro) {
        Usuario usuarioA = usuarioRepository.findByPublicId(userPublicId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        if (usuarioA.getUniqueToken().equalsIgnoreCase(tokenParceiro)) {
            throw new IllegalArgumentException("Você não pode se vincular a si mesmo.");
        }

        Usuario usuarioB = usuarioRepository.findByUniqueToken(tokenParceiro)
                .orElseThrow(() -> new IllegalArgumentException("Token do parceiro(a) não encontrado."));

        if (relacionamentoRepository.existsAtivoByUser(usuarioA)) {
            throw new IllegalArgumentException("Você já possui um relacionamento ativo.");
        }

        if (relacionamentoRepository.existsAtivoByUser(usuarioB)) {
            throw new IllegalArgumentException("O(a) parceiro(a) já possui um relacionamento ativo.");
        }

        Relacionamento rel = new Relacionamento();
        if (usuarioA.getId() < usuarioB.getId()) {
            rel.setUsuarioA(usuarioA);
            rel.setUsuarioB(usuarioB);
        } else {
            rel.setUsuarioA(usuarioB);
            rel.setUsuarioB(usuarioA);
        }
        
        relacionamentoRepository.save(rel);
    }
}
