package com.teamproject.TP_backend.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequestDTO {
    private String name;
    private String email;
    private String password;
}