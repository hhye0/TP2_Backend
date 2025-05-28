package com.teamproject.TP_backend.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record SignupRequestDTO(
        @NotBlank String name,
        @NotBlank String email,
        @NotBlank String password
) {}