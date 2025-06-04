package com.teamproject.TP_backend.controller.dto;

import com.teamproject.TP_backend.domain.entity.Notice;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

// 응답용
public record NoticeResponseDTO(
        Long id,
        String title,
        String content,
        String authorNickname
) {}

