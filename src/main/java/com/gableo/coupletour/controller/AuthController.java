package com.gableo.coupletour.controller;

import com.gableo.coupletour.dto.AuthDTO;
import com.gableo.coupletour.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public String exibirFormulario(Model model) {
        if (!model.containsAttribute("auth")) {
            model.addAttribute("auth", new AuthDTO());
        }
        return "login";
    }

    @PostMapping
    public String autenticar(@Valid @ModelAttribute("auth") AuthDTO dto,
                             BindingResult bindingResult,
                             HttpServletResponse response,
                             Model model) {

        if (bindingResult.hasErrors()) {
            return "login";
        }

        try {
            String token = authService.autenticar(dto);

            Cookie cookie = new Cookie("JWT-TOKEN", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(86400);

            response.addCookie(cookie);

            return "redirect:/feed";
        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
            return "login";
        }
    }
}
