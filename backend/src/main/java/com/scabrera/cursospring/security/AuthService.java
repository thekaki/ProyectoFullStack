package com.scabrera.cursospring.security;

import com.scabrera.cursospring.dto.LoginRequestDTO;
import com.scabrera.cursospring.dto.RegisterRequestDTO;
import com.scabrera.cursospring.dto.TokenResponseDTO;
import com.scabrera.cursospring.models.Token;
import com.scabrera.cursospring.models.Usuario;
import com.scabrera.cursospring.repository.TokenRepository;
import com.scabrera.cursospring.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenRepository tokenRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public TokenResponseDTO register(RegisterRequestDTO request) {
        var usuario = Usuario.builder()
                .nombre(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .build();
        var savedUser = usuarioRepository.save(usuario);
        var jwtToken = jwtService.generateToken(usuario);
        var refresToken = jwtService.generateRefreshToken(usuario);
        saveUserToken(savedUser, jwtToken);
        return new TokenResponseDTO(jwtToken, refresToken);
    }

    public TokenResponseDTO refresToken(final String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid Bearer token");
        }

        final String refreshToken = authHeader.substring(7);
        final String userEmail = jwtService.extractUsername(refreshToken);

        if(userEmail == null) {
            throw new IllegalArgumentException("Invalid Refresh Token");
        }

        final Usuario user = usuarioRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException(userEmail));

        if (!jwtService.isTokenValid(refreshToken, user)){
            throw new IllegalArgumentException("Invalid Refresh Token");
        }

        final String accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        return new TokenResponseDTO(accessToken, refreshToken);
    }

    public TokenResponseDTO login(LoginRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        var usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(usuario);
        var refreshToken = jwtService.generateRefreshToken(usuario);
        revokeAllUserTokens(usuario);
        saveUserToken(usuario, jwtToken);
        return new TokenResponseDTO(jwtToken, refreshToken);
    }

    private void saveUserToken(Usuario usuario, String jwtToken) {
        var token = Token.builder()
                .user(usuario)
                .token(jwtToken)
                .tokenType(Token.TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(final Usuario usuario) {
        final List<Token> validUserTokens = tokenRepository
                .findAllByUser_IdAndExpiredFalseAndRevokedFalse(usuario.getId());
        if (!validUserTokens.isEmpty()) {
            for (final Token t : validUserTokens) {
                t.setExpired(true);
                t.setRevoked(true);
            }
            tokenRepository.saveAll(validUserTokens);
        }
    }
}
