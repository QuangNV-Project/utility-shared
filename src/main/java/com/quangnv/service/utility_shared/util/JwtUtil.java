package com.quangnv.service.utility_shared.util;

import com.quangnv.service.utility_shared.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtUtil {
    JwtProperties properties;

    /* ================= KEY ================= */

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
                properties.getSecret().getBytes(StandardCharsets.UTF_8)
        );
    }

    /* ================= PARSE ================= */

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        return resolver.apply(extractAllClaims(token));
    }

    public Long extractUserId(String token) {
        return parseToken(token).get("userId", Long.class);
    }

    public String extractUserName(String token) {
        return parseToken(token).get("userName", String.class);
    }

    public List<String> extractRoles(String token) {
        List<String> roles = parseToken(token).get("roles", List.class);
        return roles == null ? List.of() : roles;
    }

    public boolean isRefreshToken(String token) {
        return "refresh".equals(parseToken(token).get("type", String.class));
    }

    public boolean isExpired(String token) {
        return parseToken(token)
                .getExpiration()
                .before(new Date());
    }

    /* ================= GENERATE ================= */

    public String generateAccessToken(Long userId, List<String> roles, String userName) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("userName", userName);
        claims.put("roles", roles);
        claims.put("type", "access");

        return createToken(
                claims,
                userId.toString(),
                properties.getAccessTokenExpiration()
        );
    }

    public String generateRefreshToken(Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "refresh");

        return createToken(
                claims,
                userId.toString(),
                properties.getRefreshTokenExpiration()
        );
    }

    private String createToken(Map<String, Object> claims, String subject, Long expiration) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .id(UUID.randomUUID().toString()) // jti
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    /* ================= VALIDATE ================= */

    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            return !isExpired(token);
        } catch (Exception e) {
            log.error("JWT invalid: {}", e.getMessage());
            return false;
        }
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}

