package com.teamproject.TP_backend.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


// 마이페이지 등에서 회원 정보 수정 요청 시 사용되는 DTO
// 수정 가능한 항목: 이름, 이메일, 비밀번호

@Getter
@Setter
public class UserUpdateRequestDTO {

    private String name; // 변경할 사용자 이름
    @Email(message = "유효한 이메일 주소를 입력하세요.")
    private String email; // 변경할 이메일 주소
    @Size(min = 6, message = "비밀번호는 최소 6자 이상이어야 합니다.")
    private String password; // 변경할 비밀번호 (선택사항)

}
