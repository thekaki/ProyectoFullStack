package com.scabrera.cursospring.repository;

import com.scabrera.cursospring.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    List<Token> findAllByUser_IdAndExpiredFalseAndRevokedFalse(Long id);

    Optional<Token> findByToken(String token);
}
