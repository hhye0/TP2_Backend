package com.teamproject.TP_backend.config.security;

import com.teamproject.TP_backend.exception.InvalidJwtException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;
    private final long EXPIRATION_MS = 1000 * 60 * 60 * 24; // 24시간 토큰 유효

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // 토큰 생성 메서드 (email, role 모두 받는 버전)
    public String createToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 인증(Authentication) 객체에서 토큰 생성 - CustomUserDetails에서 role 추출하여 호출하도록 수정
    public String generateToken(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();  // username = email로 가정
        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .orElse("ROLE_USER");  // 기본 역할 지정

        return createToken(email, role);
    }

    // 토큰에서 이메일만 추출 (예외 발생 시 InvalidJwtException 던짐)
    public String extractEmail(String token) {
        try {
            return parseClaims(token).getSubject();
        } catch (Exception e) {
            throw new InvalidJwtException("Invalid JWT token", e);
        }
    }

    // 토큰 유효성 검사 (이메일 비교 없이 서명 + 만료만 확인)
    public boolean validateToken(String token) {
        try {
            // parseClaims()가 서명 검증과 토큰 만료 확인 수행
            Claims claims = parseClaims(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            throw new InvalidJwtException("Invalid JWT token", e);
        }
    }

    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenExpired(String token) {
        try {
            Date expiration = parseClaims(token).getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return true;  // 토큰 파싱 실패 시 만료된 것으로 처리
        }
    }
}