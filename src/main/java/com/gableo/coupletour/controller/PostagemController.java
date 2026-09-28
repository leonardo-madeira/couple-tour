package com.gableo.coupletour.controller;

import com.gableo.coupletour.dto.PostagemDTO;
import com.gableo.coupletour.repository.LugarRepository;
import com.gableo.coupletour.service.PostagemService;
import com.gableo.coupletour.service.VinculacaoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/postagem")
public class PostagemController {

    private final PostagemService postagemService;
    private final LugarRepository lugarRepository;
    private final VinculacaoService vinculacaoService;

    public PostagemController(PostagemService postagemService, LugarRepository lugarRepository, VinculacaoService vinculacaoService) {
        this.postagemService = postagemService;
        this.lugarRepository = lugarRepository;
        this.vinculacaoService = vinculacaoService;
    }

    @GetMapping("/nova")
    public String novaPostagem(HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
        String publicId = (String) request.getAttribute("usuarioId");

        if (vinculacaoService.getRelacionamentoAtivo(publicId).isEmpty()) {
            redirectAttributes.addFlashAttribute("erro", "Você precisa estar vinculade a um parceire para criar postagens.");
            return "redirect:/vinculacao";
        }

        model.addAttribute("lugares", lugarRepository.findAllWithCategoria());
        model.addAttribute("postagemDTO", new PostagemDTO());
        model.addAttribute("editMode", false);
        return "nova-postagem";
    }

    @PostMapping("/nova")
    public String criarPostagem(@ModelAttribute PostagemDTO postagemDTO,
                                HttpServletRequest request,
                                RedirectAttributes redirectAttributes) {
        String publicId = (String) request.getAttribute("usuarioId");
        try {
            postagemService.criarPostagem(publicId, postagemDTO);
            redirectAttributes.addFlashAttribute("sucesso", "Avaliação publicada com sucesso!");
            return "redirect:/feed";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/postagem/nova";
        }
    }

    @GetMapping("/editar/{lugarId}")
    public String editarPostagemForm(@org.springframework.web.bind.annotation.PathVariable Long lugarId, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
        String publicId = (String) request.getAttribute("usuarioId");
        if (vinculacaoService.getRelacionamentoAtivo(publicId).isEmpty()) {
            redirectAttributes.addFlashAttribute("erro", "Você precisa estar vinculado para editar postagens.");
            return "redirect:/vinculacao";
        }
        try {
            PostagemDTO dto = postagemService.carregarParaEdicao(publicId, lugarId);
            model.addAttribute("lugares", lugarRepository.findAllWithCategoria());
            model.addAttribute("postagemDTO", dto);
            model.addAttribute("editMode", true);
            return "nova-postagem";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/feed";
        }
    }

    @org.springframework.web.bind.annotation.PutMapping("/editar/{lugarId}")
    public String salvarEdicaoPostagem(@org.springframework.web.bind.annotation.PathVariable Long lugarId,
                                       @ModelAttribute PostagemDTO postagemDTO,
                                       HttpServletRequest request,
                                       RedirectAttributes redirectAttributes) {
        String publicId = (String) request.getAttribute("usuarioId");
        try {
            postagemService.atualizarPostagem(publicId, lugarId, postagemDTO);
            redirectAttributes.addFlashAttribute("sucesso", "Avaliação atualizada com sucesso!");
            return "redirect:/feed";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/postagem/editar/" + lugarId;
        }
    }
    
    @org.springframework.web.bind.annotation.DeleteMapping("/editar/{lugarId}")
    public String deletarPostagem(@org.springframework.web.bind.annotation.PathVariable Long lugarId,
                                  jakarta.servlet.http.HttpServletRequest request,
                                  RedirectAttributes redirectAttributes) {
        String publicId = (String) request.getAttribute("usuarioId");
        try {
            postagemService.deletarPostagem(publicId, lugarId);
            redirectAttributes.addFlashAttribute("sucesso", "Avaliação excluída com sucesso!");
            return "redirect:/feed";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/postagem/editar/" + lugarId;
        }
    }
}
