package com.scabrera.cursospring.security;

import com.scabrera.cursospring.models.Token;
import com.scabrera.cursospring.models.Usuario;
import com.scabrera.cursospring.repository.TokenRepository;
import com.scabrera.cursospring.repository.UsuarioRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        log.info("JwtAuthFilter - Incoming request: {}", request.getServletPath());

        // Saltar endpoints de auth
        if (request.getServletPath().startsWith("/auth")) {
            log.info("JwtAuthFilter - Endpoint /auth, pasando filtro sin autenticar");
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("JwtAuthFilter - Authorization header ausente o inválido");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        final String jwtToken = authHeader.substring(7);
        final String userEmail;
        final Claims claims;

        try {
            claims = jwtService.extractAllClaims(jwtToken);
            userEmail = claims.getSubject();
        } catch (Exception e) {
            log.error("JwtAuthFilter - Error parsing token: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        if (userEmail == null) {
            log.warn("JwtAuthFilter - userEmail nulo, token inválido");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        final Optional<Token> tokenOpt = tokenRepository.findByToken(jwtToken);
        if (tokenOpt.isEmpty() || tokenOpt.get().isExpired() || tokenOpt.get().isRevoked()) {
            log.warn("JwtAuthFilter - Token expirado o revocado");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        final Optional<Usuario> userOpt = usuarioRepository.findByEmail(userEmail);
        if (userOpt.isEmpty()) {
            log.warn("JwtAuthFilter - Usuario no encontrado en DB");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        final Usuario user = userOpt.get();
        final boolean isTokenValid = jwtService.isTokenValid(jwtToken, user);
        if (!isTokenValid) {
            log.warn("JwtAuthFilter - Token inválido para el usuario");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Long userId = claims.get("id", Long.class);
        CustomUserDetails customUserDetails = new CustomUserDetails(
                userId,
                user.getEmail(),
                user.getPassword(),
                Collections.emptyList()
        );

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                customUserDetails,
                null,
                customUserDetails.getAuthorities()
        );
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        log.info("JwtAuthFilter - Usuario {} (ID: {}) autenticado correctamente", userEmail, userId);
        filterChain.doFilter(request, response);
    }
}
