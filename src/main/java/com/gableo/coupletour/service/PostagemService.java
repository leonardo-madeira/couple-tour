package com.gableo.coupletour.service;

import com.gableo.coupletour.dto.PostagemDTO;
import com.gableo.coupletour.model.AvaliacaoIndividual;
import com.gableo.coupletour.model.FotoAvaliacaoIndividual;
import com.gableo.coupletour.model.Lugar;
import com.gableo.coupletour.model.Relacionamento;
import com.gableo.coupletour.model.Usuario;
import com.gableo.coupletour.repository.AvaliacaoIndividualRepository;
import com.gableo.coupletour.repository.FotoAvaliacaoIndividualRepository;
import com.gableo.coupletour.repository.LugarRepository;
import com.gableo.coupletour.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class PostagemService {

    private final AvaliacaoIndividualRepository avaliacaoRepo;
    private final FotoAvaliacaoIndividualRepository fotoRepo;
    private final LugarRepository lugarRepo;
    private final UsuarioRepository usuarioRepo;
    private final VinculacaoService vinculacaoService;
    private final GcpStorageService gcpStorageService;

    public PostagemService(AvaliacaoIndividualRepository avaliacaoRepo,
                           FotoAvaliacaoIndividualRepository fotoRepo,
                           LugarRepository lugarRepo,
                           UsuarioRepository usuarioRepo,
                           VinculacaoService vinculacaoService,
                           GcpStorageService gcpStorageService) {
        this.avaliacaoRepo = avaliacaoRepo;
        this.fotoRepo = fotoRepo;
        this.lugarRepo = lugarRepo;
        this.usuarioRepo = usuarioRepo;
        this.vinculacaoService = vinculacaoService;
        this.gcpStorageService = gcpStorageService;
    }

    @Transactional
    public void criarPostagem(String usuarioPublicId, PostagemDTO dto) throws IOException {
        Usuario usuario = usuarioRepo.findByPublicId(usuarioPublicId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        Relacionamento relacionamento = vinculacaoService.getRelacionamentoAtivo(usuarioPublicId)
                .orElseThrow(() -> new IllegalArgumentException("Você precisa estar em um relacionamento para postar."));

        Lugar lugar = lugarRepo.findById(dto.getLugarId())
                .orElseThrow(() -> new IllegalArgumentException("Lugar não encontrado."));

        if (avaliacaoRepo.existsByRelacionamentoIdAndUsuarioIdAndLugarId(relacionamento.getId(), usuario.getId(), lugar.getId())) {
            throw new IllegalArgumentException("Você já avaliou este lugar neste relacionamento.");
        }

        AvaliacaoIndividual avaliacao = new AvaliacaoIndividual();
        avaliacao.setUsuario(usuario);
        avaliacao.setRelacionamento(relacionamento);
        avaliacao.setLugar(lugar);
        avaliacao.setNotaPergunta1(dto.getNota1());
        avaliacao.setNotaPergunta2(dto.getNota2());
        avaliacao.setNotaPergunta3(dto.getNota3());
        avaliacao.setNotaPergunta4(dto.getNota4());
        avaliacao.setDescricao(dto.getDescricao());
        avaliacao.setPostVisibility(dto.getPostVisibility());

        avaliacao = avaliacaoRepo.save(avaliacao);

        if (dto.getFotosRemovidas() != null && !dto.getFotosRemovidas().isEmpty()) {
            java.util.List<com.gableo.coupletour.model.FotoAvaliacaoIndividual> fotosBanco = fotoRepo.findByAvaliacaoId(avaliacao.getId());
            for (com.gableo.coupletour.model.FotoAvaliacaoIndividual foto : fotosBanco) {
                if (dto.getFotosRemovidas().contains(foto.getId())) {
                    fotoRepo.delete(foto);
                }
            }
        }

        if (dto.getFotos() != null) {
            for (MultipartFile foto : dto.getFotos()) {
                if (!foto.isEmpty()) {
                    String url = gcpStorageService.uploadImagem(foto);
                    FotoAvaliacaoIndividual fotoEntity = new FotoAvaliacaoIndividual(avaliacao, url);
                    fotoRepo.save(fotoEntity);
                }
            }
        }
    }

    public PostagemDTO carregarParaEdicao(String usuarioPublicId, Long lugarId) {
        Usuario usuario = usuarioRepo.findByPublicId(usuarioPublicId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
        Relacionamento rel = vinculacaoService.getRelacionamentoAtivo(usuarioPublicId)
                .orElseThrow(() -> new IllegalArgumentException("Relacionamento não encontrado."));

        AvaliacaoIndividual avaliacao = avaliacaoRepo.findByRelacionamentoIdAndUsuarioIdAndLugarId(rel.getId(), usuario.getId(), lugarId)
                .orElseThrow(() -> new IllegalArgumentException("Avaliação não encontrada."));

        PostagemDTO dto = new PostagemDTO();
        dto.setLugarId(lugarId);
        dto.setNota1(avaliacao.getNotaPergunta1());
        dto.setNota2(avaliacao.getNotaPergunta2());
        dto.setNota3(avaliacao.getNotaPergunta3());
        dto.setNota4(avaliacao.getNotaPergunta4());
        dto.setDescricao(avaliacao.getDescricao());
        dto.setPostVisibility(avaliacao.getPostVisibility());
        java.util.List<com.gableo.coupletour.model.FotoAvaliacaoIndividual> fotos = fotoRepo.findByAvaliacaoId(avaliacao.getId());
        dto.setFotosExistentes(fotos.stream().map(f -> new PostagemDTO.FotoDTO(f.getId(), f.getUrl())).collect(java.util.stream.Collectors.toList()));
        return dto;
    }

    @Transactional
    public void atualizarPostagem(String usuarioPublicId, Long lugarId, PostagemDTO dto) throws IOException {
        Usuario usuario = usuarioRepo.findByPublicId(usuarioPublicId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
        Relacionamento rel = vinculacaoService.getRelacionamentoAtivo(usuarioPublicId)
                .orElseThrow(() -> new IllegalArgumentException("Relacionamento não encontrado."));

        AvaliacaoIndividual avaliacao = avaliacaoRepo.findByRelacionamentoIdAndUsuarioIdAndLugarId(rel.getId(), usuario.getId(), lugarId)
                .orElseThrow(() -> new IllegalArgumentException("Avaliação não encontrada."));

        avaliacao.setNotaPergunta1(dto.getNota1());
        avaliacao.setNotaPergunta2(dto.getNota2());
        avaliacao.setNotaPergunta3(dto.getNota3());
        avaliacao.setNotaPergunta4(dto.getNota4());
        avaliacao.setDescricao(dto.getDescricao());
        avaliacao.setPostVisibility(dto.getPostVisibility());

        avaliacaoRepo.save(avaliacao);

        if (dto.getFotosRemovidas() != null && !dto.getFotosRemovidas().isEmpty()) {
            java.util.List<com.gableo.coupletour.model.FotoAvaliacaoIndividual> fotosBanco = fotoRepo.findByAvaliacaoId(avaliacao.getId());
            for (com.gableo.coupletour.model.FotoAvaliacaoIndividual foto : fotosBanco) {
                if (dto.getFotosRemovidas().contains(foto.getId())) {
                    fotoRepo.delete(foto);
                }
            }
        }

        if (dto.getFotos() != null) {
            for (MultipartFile foto : dto.getFotos()) {
                if (!foto.isEmpty()) {
                    String url = gcpStorageService.uploadImagem(foto);
                    FotoAvaliacaoIndividual fotoEntity = new FotoAvaliacaoIndividual(avaliacao, url);
                    fotoRepo.save(fotoEntity);
                }
            }
        }
    }
    
    @Transactional
    public void deletarPostagem(String usuarioPublicId, Long lugarId) {
        Usuario usuario = usuarioRepo.findByPublicId(usuarioPublicId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
        Relacionamento rel = vinculacaoService.getRelacionamentoAtivo(usuarioPublicId)
                .orElseThrow(() -> new IllegalArgumentException("Relacionamento não encontrado."));

        AvaliacaoIndividual avaliacao = avaliacaoRepo.findByRelacionamentoIdAndUsuarioIdAndLugarId(rel.getId(), usuario.getId(), lugarId)
                .orElseThrow(() -> new IllegalArgumentException("Avaliação não encontrada."));

        avaliacaoRepo.delete(avaliacao);
    }
}
