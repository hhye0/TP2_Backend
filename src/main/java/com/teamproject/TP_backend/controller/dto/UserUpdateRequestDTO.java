package com.teamproject.TP_backend.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequestDTO {
    private String name;
    @Email(message = "유효한 이메일 주소를 입력하세요.")
    private String email;

    @Size(min = 6, message = "비밀번호는 최소 6자 이상이어야 합니다.")
    private String password;

}