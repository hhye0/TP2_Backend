package com.teamproject.TP_backend.controller.dto;

import jakarta.validation.constraints.NotBlank;

//사용자의 로그인 요청 정보를 담는 DTO
// Spring의 @RequestBody로 전달받아 로그인 인증 처리에 사용

public record LoginRequestDTO(

        @NotBlank(message = "이메일은 필수입니다.")
        String email,         // 사용자 이메일 (로그인 ID 역할)

        @NotBlank(message = "비밀번호는 필수입니다.")
        String password       // 사용자 비밀번호

) {}
