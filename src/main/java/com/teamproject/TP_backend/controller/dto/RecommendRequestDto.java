package com.teamproject.TP_backend.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RecommendRequestDto {
    private Long toUserId;
    private BookDTO book;
    private String message;
}
