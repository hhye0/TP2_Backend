package com.teamproject.TP_backend.controller.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

// 응답용 DTO (닉네임 포함)
@Data
@Builder
public class BookReviewResponseDTO {
    private Long reviewId;
    private String bookTitle;
    private String bookAuthor;
    private String bookCoverUrl;
    private String message;
    private String reviewerNickname;  // 추가
    private LocalDateTime createdAt;
}