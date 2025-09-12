package com.scabrera.cursospring.config;

import com.scabrera.cursospring.security.JwtAuthFilter;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Argon2PasswordEncoder(16, 32, 2, 65536, 3);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                // No deshabilitamos anonymous: queremos que el front sea accesible libremente
                .authorizeHttpRequests(auth -> auth
                        // Recursos estáticos (css/js/img/webjars...) - permitidos
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        // Endpoints de autenticación (login/register/refresh) - permitidos
                        .requestMatchers("/auth/**").permitAll()
                        // APIs protegidas: requieren Authentication (JWT)
                        .requestMatchers("/api/**").authenticated()
                        // TODO: otras rutas no-API se permiten (la UI se sirve sin auth)
                        .anyRequest().permitAll()
                )
                // Nuestro filtro JWT — solo pone Authentication si llega token válido
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
