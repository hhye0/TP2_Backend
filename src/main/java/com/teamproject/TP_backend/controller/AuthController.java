package com.teamproject.TP_backend.controller;

import com.teamproject.TP_backend.config.security.JwtUtil;
import com.teamproject.TP_backend.controller.dto.LoginRequestDTO;
import com.teamproject.TP_backend.controller.dto.SignupRequestDTO;
import com.teamproject.TP_backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

// 인증 및 회원가입 관련 API를 처리하는 컨트롤러
// 경로: /api/auth
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager; // 스프링 시큐리티 인증 처리 매니저
    private final JwtUtil jwtUtil;                             // JWT 토큰 생성 유틸리티
    private final UserService userService;                     // 사용자 관련 서비스 로직 처리

    //     로그인 처리 엔드포인트
    //     - 이메일/비밀번호로 인증을 시도하고 성공 시 JWT 토큰 발급
    //     @param dto 로그인 요청 DTO (email, password 포함)
    //     @return JWT 토큰을 포함한 응답
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO dto) {
        // 인증 객체 생성 및 검증
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.email(),        // 사용자 이메일
                        dto.password()      // 사용자 비밀번호
                )
        );

        // 인증 성공 시 토큰 생성
        String token = jwtUtil.generateToken(authentication);

        // 토큰을 JSON 형태로 반환
        return ResponseEntity.ok(Map.of("token", token));
    }

    // 회원가입 처리 엔드포인트
    // - 사용자 정보를 DB에 저장
    // @param dto 회원가입 요청 DTO (name, email, password 포함)
    // @return 성공 메시지 반환
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid SignupRequestDTO dto) {
        userService.signup(dto);
        return ResponseEntity.ok(Map.of("message", "회원가입 성공"));
    }
}
