package com.scabrera.cursospring.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JWTUtil {

    @Value("${security.jwt.secret}")
    private String key;

    @Value("${security.jwt.issuer}")
    private String issuer;

    private SecretKey getSigningKey() {
        byte[] apiKeySecretBytes = Base64.getDecoder().decode(this.key);
        return Keys.hmacShaKeyFor(apiKeySecretBytes);
    }

    public String create(String userId, String email, long ttlMillis) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        JwtBuilder builder = Jwts.builder()
                .setId(userId)
                .setSubject(email)
                .setIssuer(issuer)
                .setIssuedAt(now)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256);

        if (ttlMillis > 0) {
            builder.setExpiration(new Date(nowMillis + ttlMillis));
        }

        return builder.compact();
    }

    public String getEmailFromToken(String jwt) {
        return parseClaims(jwt).getSubject();
    }

    public String getUserIdFromToken(String jwt) {
        return parseClaims(jwt).getId();
    }

    private Claims parseClaims(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
