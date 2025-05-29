package com.teamproject.TP_backend.controller.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class MeetingDTO {
    private Long id;
    private String title;
    private String bookTitle;
    private String bookAuthor;
    private String bookCover;
    private String bookCategory;
    private LocalDateTime startDate;
    private int maxMembers;
    private boolean active;
    private String hostName; // ✅ 추가된 필드

    private List<ParticipantDTO> participants;
}
