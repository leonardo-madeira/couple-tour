package com.gableo.coupletour.controller;

import com.gableo.coupletour.service.FeedService;
import com.gableo.coupletour.repository.CategoriaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FeedController {

    private final FeedService feedService;
    private final CategoriaRepository categoriaRepository;

    public FeedController(FeedService feedService, CategoriaRepository categoriaRepository) {
        this.feedService = feedService;
        this.categoriaRepository = categoriaRepository;
    }

    @GetMapping("/feed")
    public String feed(@RequestParam(required = false) Long categoriaId, Model model, jakarta.servlet.http.HttpServletRequest request) {
        model.addAttribute("posts", feedService.getFeed(categoriaId));
        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("categoriaSelecionada", categoriaId);
        model.addAttribute("logadoPublicId", request.getAttribute("usuarioId"));
        return "feed";
    }
}
