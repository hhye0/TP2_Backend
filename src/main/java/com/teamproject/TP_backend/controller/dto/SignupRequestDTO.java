package com.teamproject.TP_backend.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignupRequestDTO(
        @NotBlank(message = "이름은 필수입니다.") String name,
        @Email(message = "유효한 이메일 주소를 입력하세요.") @NotBlank String email,
        @NotBlank String password
) {}