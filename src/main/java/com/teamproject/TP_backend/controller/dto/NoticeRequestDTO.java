package com.teamproject.TP_backend.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 등록/ 수정 요청용
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeRequestDTO {

    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    @NotBlank(message = "내용은 필수입니다.")
    private String content;

    private String authorNickname;  // 작성자 닉네임 (필요시)
}