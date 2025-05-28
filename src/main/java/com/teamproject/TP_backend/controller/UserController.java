package com.teamproject.TP_backend.controller;

import com.teamproject.TP_backend.controller.dto.LoginRequestDTO;
import com.teamproject.TP_backend.controller.dto.SignupRequestDTO;
import com.teamproject.TP_backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody @Valid SignupRequestDTO dto) {
        userService.signup(dto);
        return ResponseEntity.ok("회원가입 성공");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO dto) {
        String result = userService.login(dto);  // String 반환받음

        if ("로그인 성공".equals(result)) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }
    }
}
