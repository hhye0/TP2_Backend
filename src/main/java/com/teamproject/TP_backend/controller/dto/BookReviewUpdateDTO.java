package com.teamproject.TP_backend.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 리뷰 수정용 DTO
@Getter
@NoArgsConstructor
public class BookReviewUpdateDTO {
    @NotBlank(message = "리뷰 내용은 필수입니다.")
    @Size(max = 500, message = "리뷰는 500자 이내로 작성해주세요.")
    private String message;
}