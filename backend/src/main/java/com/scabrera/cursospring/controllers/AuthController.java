package com.scabrera.cursospring.controllers;

import com.scabrera.cursospring.dto.RegisterLoginDTO;
import com.scabrera.cursospring.dto.TokenResponseDTO;
import com.scabrera.cursospring.models.Usuario;
import com.scabrera.cursospring.repository.UsuarioRepository;
import com.scabrera.cursospring.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtil jwtUtil;

    @PostMapping("/login")
    public TokenResponseDTO login(@RequestBody RegisterLoginDTO userLogin) {
        Usuario usuario = usuarioRepository.findByEmail(userLogin.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(userLogin.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        String accessToken = jwtUtil.create(String.valueOf(usuario.getId()), usuario.getEmail(), 15 * 60 * 1000);
        String refreshToken = jwtUtil.create(String.valueOf(usuario.getId()), usuario.getEmail(), 7 * 24 * 60 * 60 * 1000);

        // NO CREAMOS COOKIE — devolvemos tokens en JSON
        return new TokenResponseDTO(accessToken, refreshToken, "Bearer", 15 * 60L);
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterLoginDTO userRegister) {
        if (usuarioRepository.findByEmail(userRegister.getEmail()).isPresent()) {
            return "El usuario ya existe";
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(userRegister.getEmail());
        usuario.setPassword(passwordEncoder.encode(userRegister.getPassword()));
        usuarioRepository.save(usuario);

        return "Usuario registrado correctamente";
    }

    @PostMapping("/refresh")
    public TokenResponseDTO refresh(@RequestBody String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new RuntimeException("Refresh token inválido o expirado");
        }

        String userId = jwtUtil.getUserIdFromToken(refreshToken);
        String email = jwtUtil.getEmailFromToken(refreshToken);

        String newAccessToken = jwtUtil.create(userId, email, 15 * 60 * 1000);

        // NO actualizamos cookie aquí — devolvemos JSON
        return new TokenResponseDTO(newAccessToken, refreshToken, "Bearer", 15 * 60L);
    }
}
