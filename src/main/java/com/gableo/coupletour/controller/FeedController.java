package com.gableo.coupletour.controller;

import com.gableo.coupletour.service.FeedService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FeedController {

    private final FeedService feedService;

    public FeedController(FeedService feedService) {
        this.feedService = feedService;
    }

    @GetMapping("/feed")
    public String feed(Model model, jakarta.servlet.http.HttpServletRequest request) {
        model.addAttribute("posts", feedService.getFeedGeral());
        model.addAttribute("logadoPublicId", request.getAttribute("usuarioId"));
        return "feed";
    }
}
