package com.teamproject.TP_backend.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

// 회원가입 요청 시 클라이언트가 전달하는 정보를 담는 DTO
// @RequestBody로 바인딩되어 UserService에서 사용

public record SignupRequestDTO(
        @NotBlank(message = "이름은 필수입니다.")
        String name,         // 사용자 이름
        @NotBlank(message = "닉네임을 입력하세요.")
        String nickName,
        @Email(message = "유효한 이메일 주소를 입력하세요.")
        @NotBlank
        String email,        // 사용자 이메일 (아이디로 사용됨)
        @NotBlank
        String password      // 사용자 비밀번호
) {}
