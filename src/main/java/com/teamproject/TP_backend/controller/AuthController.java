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

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO dto) {
        // 1. 이메일과 비밀번호로 인증 시도
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.email(),
                        dto.password()
                )
        );

        // 2. 인증 성공 시 JWT 발급
        String token = jwtUtil.generateToken(authentication);

        // 3. 응답에 JWT 포함하여 반환
        Map<String, String> response = new HashMap<>();
        response.put("token", token);

        return ResponseEntity.ok(response);
    }



    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid SignupRequestDTO dto) {
        userService.signup(dto);
        return ResponseEntity.ok("회원가입 성공");
    }

}