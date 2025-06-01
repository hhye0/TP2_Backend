package com.teamproject.TP_backend.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookReviewRequestDto {
    private String bookTitle;
    private String message;
    private Long toUserId;
}
