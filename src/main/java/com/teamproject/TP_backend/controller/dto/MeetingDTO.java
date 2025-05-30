package com.teamproject.TP_backend.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
public class MeetingDTO {
    private Long id;

    @NotBlank(message = "제목은 필수입니다.")
    private String title;
    private String bookTitle;
    private String bookAuthor;
    private String bookCover;
    private String bookCategory;
    private LocalDateTime startDate;
    private int maxMembers;
    private boolean active;
    private List<ParticipantDTO> participants;
    private Long hostId; // host_id 전달을 위한 필드 추가
}
