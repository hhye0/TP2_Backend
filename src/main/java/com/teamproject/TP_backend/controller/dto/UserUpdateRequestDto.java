package com.teamproject.TP_backend.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequestDto {
    private String name;
    private String email;
    private String password;
}