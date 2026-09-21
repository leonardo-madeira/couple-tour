package com.gableo.coupletour.service;

import com.gableo.coupletour.dto.UsuarioCadastroDTO;
import com.gableo.coupletour.model.Pronome;
import com.gableo.coupletour.model.Usuario;
import com.gableo.coupletour.repository.PronomeRepository;
import com.gableo.coupletour.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;

@Service
public class UsuarioService {

    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private final UsuarioRepository usuarioRepository;
    private final PronomeRepository pronomeRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PronomeRepository pronomeRepository) {
        this.usuarioRepository = usuarioRepository;
        this.pronomeRepository = pronomeRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Transactional
    public Usuario cadastrar(UsuarioCadastroDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("E-mail já cadastrado no sistema.");
        }

        Pronome pronome = pronomeRepository.findById(dto.getPronomeId())
                .orElseThrow(() -> new IllegalArgumentException("Pronome inválido."));

        String senhaHash = passwordEncoder.encode(dto.getSenha());
        String uniqueToken = gerarUniqueTokenUnico();

        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenhaHash(senhaHash);
        usuario.setDataNascimento(dto.getDataNascimento());
        usuario.setPronome(pronome);
        usuario.setUniqueToken(uniqueToken);

        return usuarioRepository.save(usuario);
    }

    private String gerarUniqueTokenUnico() {
        String token;
        do {
            token = gerarTokenAleatorio();
        } while (usuarioRepository.existsByUniqueToken(token));
        return token;
    }

    private String gerarTokenAleatorio() {
        StringBuilder sb = new StringBuilder(4);
        for (int i = 0; i < 4; i++) {
            sb.append(ALPHANUMERIC.charAt(RANDOM.nextInt(ALPHANUMERIC.length())));
        }
        return sb.toString();
    }
}
