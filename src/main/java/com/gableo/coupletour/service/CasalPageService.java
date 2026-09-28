package com.gableo.coupletour.service;

import com.gableo.coupletour.dto.CasalPageEditDTO;
import com.gableo.coupletour.model.CasalPageRespostas;
import com.gableo.coupletour.model.Relacionamento;
import com.gableo.coupletour.model.Usuario;
import com.gableo.coupletour.repository.CasalPageRespostasRepository;
import com.gableo.coupletour.repository.SeguidorRepository;
import com.gableo.coupletour.model.Seguidor;
import com.gableo.coupletour.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
public class CasalPageService {

    private final CasalPageRespostasRepository respostasRepo;
    private final VinculacaoService vinculacaoService;
    private final UsuarioRepository usuarioRepo;
    private final GcpStorageService gcpStorageService;
    private final com.gableo.coupletour.repository.FeedRepository feedRepo;
    private final SeguidorRepository seguidorRepo;

    public CasalPageService(CasalPageRespostasRepository respostasRepo,
                            VinculacaoService vinculacaoService,
                            UsuarioRepository usuarioRepo,
                            GcpStorageService gcpStorageService,
                            com.gableo.coupletour.repository.FeedRepository feedRepo,
                            SeguidorRepository seguidorRepo) {
        this.respostasRepo = respostasRepo;
        this.vinculacaoService = vinculacaoService;
        this.usuarioRepo = usuarioRepo;
        this.gcpStorageService = gcpStorageService;
        this.feedRepo = feedRepo;
        this.seguidorRepo = seguidorRepo;
    }

    public CasalPageEditDTO carregarDadosEdicao(String publicIdLogado) {
        Relacionamento rel = vinculacaoService.getRelacionamentoAtivo(publicIdLogado)
                .orElseThrow(() -> new IllegalArgumentException("Você não está em um relacionamento."));

        Usuario eu = usuarioRepo.findByPublicId(publicIdLogado).get();
        Usuario parceiro = rel.getUsuarioA().getId().equals(eu.getId()) ? rel.getUsuarioB() : rel.getUsuarioA();

        CasalPageRespostas minhasRespostas = respostasRepo.findByRelacionamentoIdAndUsuarioId(rel.getId(), eu.getId())
                .orElse(new CasalPageRespostas());

        CasalPageEditDTO dto = new CasalPageEditDTO();
        dto.setResposta1(minhasRespostas.getResposta1());
        dto.setResposta2(minhasRespostas.getResposta2());
        dto.setResposta3(minhasRespostas.getResposta3());

        String eleEla = parceiro.getPronome() != null ? parceiro.getPronome().getTerceiraPessoa() : "Ele(a)";
        String deleDela = parceiro.getPronome() != null ? parceiro.getPronome().getPosse() : "dele(a)";

        dto.setLabel1("Qual a mania mais engraçada " + deleDela.toLowerCase() + "?");
        dto.setLabel2("O que você mais admira n" + eleEla.toLowerCase() + "?");
        dto.setLabel3("Qual o date favorito " + deleDela.toLowerCase() + "?");

        return dto;
    }

    @Transactional
    public void salvarRespostas(String publicIdLogado, CasalPageEditDTO dto) throws IOException {
        Relacionamento rel = vinculacaoService.getRelacionamentoAtivo(publicIdLogado)
                .orElseThrow(() -> new IllegalArgumentException("Você não está em um relacionamento."));

        Usuario eu = usuarioRepo.findByPublicId(publicIdLogado).get();

        CasalPageRespostas respostas = respostasRepo.findByRelacionamentoIdAndUsuarioId(rel.getId(), eu.getId())
                .orElse(new CasalPageRespostas());

        respostas.setUsuario(eu);
        respostas.setRelacionamento(rel);
        respostas.setResposta1(dto.getResposta1());
        respostas.setResposta2(dto.getResposta2());
        respostas.setResposta3(dto.getResposta3());

        if (dto.getFotoPerfil() != null && !dto.getFotoPerfil().isEmpty()) {
            String url = gcpStorageService.uploadImagem(dto.getFotoPerfil());
            respostas.setFotoPerfilUrl(url);
        }

        respostasRepo.save(respostas);
    }

    public com.gableo.coupletour.dto.CasalPageViewDTO carregarPerfilPublico(String targetPublicId, String logadoPublicId) {
        Usuario targetUser = usuarioRepo.findByPublicId(targetPublicId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        Relacionamento rel = vinculacaoService.getRelacionamentoAtivo(targetPublicId)
                .orElseThrow(() -> new IllegalArgumentException("O usuário não está em um relacionamento ativo."));

        Usuario userA = rel.getUsuarioA();
        Usuario userB = rel.getUsuarioB();

        com.gableo.coupletour.dto.CasalPageViewDTO view = new com.gableo.coupletour.dto.CasalPageViewDTO();
        view.setNomeA(userA.getNome());
        view.setNomeB(userB.getNome());

        view.setPodeEditarA(userA.getPublicId().equals(logadoPublicId));
        view.setPodeEditarB(userB.getPublicId().equals(logadoPublicId));
        
        if (logadoPublicId != null) {
            usuarioRepo.findByPublicId(logadoPublicId).ifPresent(logado -> {
                view.setSeguindo(seguidorRepo.existsByRelacionamentoAndSeguidor(rel, logado));
            });
        }
        view.setContagemSeguidores(seguidorRepo.countByRelacionamento(rel));
        view.setContagemSeguidores(seguidorRepo.countByRelacionamento(rel));

        respostasRepo.findByRelacionamentoIdAndUsuarioId(rel.getId(), userA.getId()).ifPresent(respA -> {
            view.setFotoAUrl(respA.getFotoPerfilUrl());
            view.setRespostaA1(respA.getResposta1());
            view.setRespostaA2(respA.getResposta2());
            view.setRespostaA3(respA.getResposta3());
        });

        respostasRepo.findByRelacionamentoIdAndUsuarioId(rel.getId(), userB.getId()).ifPresent(respB -> {
            view.setFotoBUrl(respB.getFotoPerfilUrl());
            view.setRespostaB1(respB.getResposta1());
            view.setRespostaB2(respB.getResposta2());
            view.setRespostaB3(respB.getResposta3());
        });

        String eleElaB = userB.getPronome() != null ? userB.getPronome().getTerceiraPessoa() : "Ele(a)";
        String deleDelaB = userB.getPronome() != null ? userB.getPronome().getPosse() : "dele(a)";
        view.setLabel1ParaB("A mania mais engraçada " + deleDelaB.toLowerCase());
        view.setLabel2ParaB("O que " + userA.getNome() + " mais admira n" + eleElaB.toLowerCase());
        view.setLabel3ParaB("O date favorito " + deleDelaB.toLowerCase());

        String eleElaA = userA.getPronome() != null ? userA.getPronome().getTerceiraPessoa() : "Ele(a)";
        String deleDelaA = userA.getPronome() != null ? userA.getPronome().getPosse() : "dele(a)";
        view.setLabel1ParaA("A mania mais engraçada " + deleDelaA.toLowerCase());
        view.setLabel2ParaA("O que " + userB.getNome() + " mais admira n" + eleElaA.toLowerCase());
        view.setLabel3ParaA("O date favorito " + deleDelaA.toLowerCase());

        view.setFeedCasal(feedRepo.getFeedDoCasal(rel.getId()));
        view.setCasaisSeguidos(seguidorRepo.findCasaisSeguidos(userA.getId(), userB.getId()));

        return view;
    }
    
    @Transactional
    public void toggleSeguir(String logadoId, String targetPublicId) {
        Usuario logado = usuarioRepo.findByPublicId(logadoId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário logado não encontrado."));
        Relacionamento rel = vinculacaoService.getRelacionamentoAtivo(targetPublicId)
                .orElseThrow(() -> new IllegalArgumentException("O usuário não está em um relacionamento ativo."));
        
        if (rel.getUsuarioA().getId().equals(logado.getId()) || rel.getUsuarioB().getId().equals(logado.getId())) {
            throw new IllegalArgumentException("Você não pode seguir seu próprio relacionamento.");
        }

        seguidorRepo.findByRelacionamentoAndSeguidor(rel, logado).ifPresentOrElse(
            seguidorRepo::delete,
            () -> seguidorRepo.save(new Seguidor(rel, logado))
        );
    }
}
