// ApplicantDTO.java
package com.teamproject.TP_backend.controller.dto;

import com.teamproject.TP_backend.domain.enums.ParticipationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

// 수락되지 않은 신청자 조회
@Data
@AllArgsConstructor
public class ApplicantDTO {
    private Long userId;
    private String nickname;
    private String email;
    private ParticipationStatus status;
}
