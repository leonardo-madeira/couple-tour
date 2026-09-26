package com.gableo.coupletour.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/vinculacao")
public class VinculacaoController {

    private final com.gableo.coupletour.service.VinculacaoService vinculacaoService;

    public VinculacaoController(com.gableo.coupletour.service.VinculacaoService vinculacaoService) {
        this.vinculacaoService = vinculacaoService;
    }

    @GetMapping
    public String exibirVinculacao(jakarta.servlet.http.HttpServletRequest request, org.springframework.ui.Model model) {
        model.addAttribute("usuarioNome", request.getAttribute("usuarioNome"));
        model.addAttribute("usuarioUniqueToken", request.getAttribute("usuarioUniqueToken"));
        return "vinculacao";
    }

    @org.springframework.web.bind.annotation.PostMapping
    public String realizarVinculacao(@org.springframework.web.bind.annotation.RequestParam("tokenParceiro") String tokenParceiro,
                                     jakarta.servlet.http.HttpServletRequest request,
                                     org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        String publicId = (String) request.getAttribute("usuarioId");
        try {
            vinculacaoService.vincular(publicId, tokenParceiro);
            redirectAttributes.addFlashAttribute("sucesso", "Vínculo realizado com sucesso!");
            return "redirect:/feed";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/vinculacao";
        }
    }
}
