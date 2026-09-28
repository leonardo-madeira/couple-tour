package com.gableo.coupletour.controller;

import com.gableo.coupletour.dto.CasalPageEditDTO;
import com.gableo.coupletour.service.CasalPageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/casal")
public class CasalPageController {

    private final CasalPageService casalPageService;

    public CasalPageController(CasalPageService casalPageService) {
        this.casalPageService = casalPageService;
    }

    @GetMapping("/meu-perfil")
    public String redirecionarMeuPerfil(HttpServletRequest request) {
        String logadoId = (String) request.getAttribute("usuarioId");
        return "redirect:/casal/" + logadoId;
    }

    @GetMapping("/editar")
    public String editarPerfil(HttpServletRequest request, Model model) {
        String publicId = (String) request.getAttribute("usuarioId");
        try {
            CasalPageEditDTO dto = casalPageService.carregarDadosEdicao(publicId);
            model.addAttribute("editDTO", dto);
            return "casal-editar";
        } catch (IllegalArgumentException e) {
            return "redirect:/vinculacao";
        }
    }

    @GetMapping("/{publicId}")
    public String verPerfil(@PathVariable String publicId, HttpServletRequest request, Model model) {
        String logadoId = (String) request.getAttribute("usuarioId");
        try {
            com.gableo.coupletour.dto.CasalPageViewDTO view = casalPageService.carregarPerfilPublico(publicId, logadoId);
            model.addAttribute("perfil", view);
            model.addAttribute("logadoPublicId", logadoId);

            if (view.isPodeEditarA() || view.isPodeEditarB()) {
                CasalPageEditDTO editDTO = casalPageService.carregarDadosEdicao(logadoId);
                model.addAttribute("editDTO", editDTO);
            }

            return "casal-perfil";
        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
            return "feed"; 
        }
    }

    @PostMapping("/editar")
    public String salvarPerfil(@ModelAttribute CasalPageEditDTO dto,
                               HttpServletRequest request,
                               RedirectAttributes redirectAttributes) {
        String publicId = (String) request.getAttribute("usuarioId");
        try {
            casalPageService.salvarRespostas(publicId, dto);
            redirectAttributes.addFlashAttribute("sucesso", "Perfil atualizado com sucesso!");
            return "redirect:/casal/" + publicId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao salvar perfil: " + e.getMessage());
            return "redirect:/casal/editar";
        }
    }
}
