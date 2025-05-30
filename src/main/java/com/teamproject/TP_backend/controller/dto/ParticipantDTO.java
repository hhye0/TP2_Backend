package com.teamproject.TP_backend.controller.dto;

import com.teamproject.TP_backend.domain.enums.ParticipantStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParticipantDTO {
    private Long userId;
    private String userName;
    private ParticipantStatus status;
}
