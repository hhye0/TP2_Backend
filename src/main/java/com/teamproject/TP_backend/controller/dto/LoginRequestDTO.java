package com.teamproject.TP_backend.controller.dto;

import jakarta.validation.constraints.NotBlank;


public record LoginRequestDTO(
    @NotBlank(message = "이메일은 필수입니다.") String email,
    @NotBlank(message = "비밀번호는 필수입니다.") String password
) {}