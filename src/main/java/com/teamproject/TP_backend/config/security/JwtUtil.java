package com.teamproject.TP_backend.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "mysecretkeymysecretkeymysecretkey123!"; // 32바이트 이상 추천
    private final long EXPIRATION_MS = 1000 * 60 * 60 * 24; // 24시간 토큰 유효

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // JwtUtil.java 에 추가
    public String generateToken(Authentication authentication) {
        // 인증 객체에서 UserDetails를 꺼내 이메일(또는 username) 얻기
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();  // 보통 username은 email

        return createToken(email);  // 기존에 만든 createToken(email) 메서드 재활용
    }

    // 토큰 생성
    public String createToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰에서 이메일 추출
    public String extractEmail(String token) {
        return parseClaims(token).getSubject();
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token, String username) {
        try {
            String extractedUsername = extractEmail(token);
            return extractedUsername.equals(username) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
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