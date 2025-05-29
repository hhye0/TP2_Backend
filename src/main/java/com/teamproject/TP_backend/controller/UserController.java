package com.teamproject.TP_backend.controller;

import com.teamproject.TP_backend.config.security.CustomUserDetails;
import com.teamproject.TP_backend.controller.dto.UserUpdateRequestDTO;
import com.teamproject.TP_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PatchMapping("/me")
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateRequestDTO dto, Authentication authentication) {
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getUser().getId();
        boolean updated = userService.updateUser(userId, dto);
        if (updated) {
            return ResponseEntity.ok("회원정보 수정 완료");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("회원 정보를 찾을 수 없습니다.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            return ResponseEntity.ok("계정 탈퇴 완료");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("회원 정보를 찾을 수 없습니다.");
        }
    }
}