package com.scabrera.cursospring.controllers;

import com.scabrera.cursospring.dto.LoginRequestDTO;
import com.scabrera.cursospring.dto.RegisterRequestDTO;
import com.scabrera.cursospring.dto.TokenResponseDTO;
import com.scabrera.cursospring.security.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<TokenResponseDTO> register(@RequestBody final RegisterRequestDTO request) {
        final TokenResponseDTO token = authService.register(request);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody final LoginRequestDTO request) {
        final TokenResponseDTO token = authService.login(request);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/refresh")
    public TokenResponseDTO refreshToken(@RequestHeader(HttpHeaders.AUTHORIZATION) final String authHeader) {
        return authService.refresToken(authHeader);
    }
}
