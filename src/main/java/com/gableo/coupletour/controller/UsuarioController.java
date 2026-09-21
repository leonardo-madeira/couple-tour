package com.gableo.coupletour.controller;

import com.gableo.coupletour.dto.UsuarioCadastroDTO;
import com.gableo.coupletour.repository.PronomeRepository;
import com.gableo.coupletour.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cadastro")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final PronomeRepository pronomeRepository;

    public UsuarioController(UsuarioService usuarioService, PronomeRepository pronomeRepository) {
        this.usuarioService = usuarioService;
        this.pronomeRepository = pronomeRepository;
    }

    @GetMapping
    public String exibirFormulario(Model model) {
        if (!model.containsAttribute("usuario")) {
            model.addAttribute("usuario", new UsuarioCadastroDTO());
        }
        model.addAttribute("pronomesList", pronomeRepository.findAll());
        return "cadastro";
    }

    @PostMapping
    public String cadastrar(@Valid @ModelAttribute("usuario") UsuarioCadastroDTO dto,
                            BindingResult bindingResult,
                            Model model,
                            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("pronomesList", pronomeRepository.findAll());
            return "cadastro";
        }

        try {
            usuarioService.cadastrar(dto);
            redirectAttributes.addFlashAttribute("sucesso", "Cadastro realizado com sucesso!");
            return "redirect:/cadastro";
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("email", "error.usuario", e.getMessage());
            model.addAttribute("pronomesList", pronomeRepository.findAll());
            return "cadastro";
        }
    }
}
