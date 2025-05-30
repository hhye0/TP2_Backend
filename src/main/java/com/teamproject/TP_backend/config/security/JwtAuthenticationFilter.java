package com.teamproject.TP_backend.config.security;

import com.teamproject.TP_backend.exception.InvalidJwtException;
import com.teamproject.TP_backend.repository.UserRepository;
import com.teamproject.TP_backend.service.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import com.teamproject.TP_backend.domain.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            try {
                if (jwtUtil.validateToken(token)) {
                    String email = jwtUtil.extractEmail(token);
                    Claims claims = jwtUtil.parseClaims(token);

                    String role = claims.get("role", String.class);
                    List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

                    // ✅ 진짜 DB에서 유저 꺼내기
                    com.teamproject.TP_backend.domain.entity.User user = userRepository.findByEmail(email)
                            .orElseThrow(() -> new RuntimeException("유저 없음: " + email));

                    // ✅ CustomUserDetails 생성
                    CustomUserDetails userDetails = new CustomUserDetails(user);

                    // ✅ 인증 객체 만들기
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // ✅ 여기만 유지!!!
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                // 토큰이 유효하지 않으면 SecurityContext에 인증 정보 설정 안 함 (익셉션 로그 등 필요 시 여기에 작성)
            }
        }

        filterChain.doFilter(request, response);
    }
}