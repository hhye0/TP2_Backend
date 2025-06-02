package com.teamproject.TP_backend.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookReviewRequestDTO {
    @NotBlank(message = "책 제목은 필수입니다.")
    private String bookTitle;

    @NotBlank(message = "내용은 필수입니다.")
    @Size(max = 300, message = "리뷰는 300자 이내로 작성해주세요.")
    private String message;
}
