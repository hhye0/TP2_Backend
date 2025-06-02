package com.teamproject.TP_backend.controller.dto;

import com.teamproject.TP_backend.domain.enums.ParticipationStatus;
import lombok.Builder;
import lombok.Data;

// 모임 참가자 정보를 담는 DTO
// 모임 조회 시 참가자 목록에 포함되어 전달

@Data
@Builder
public class ParticipantDTO {
    private Long userId;           // 참가자 사용자 ID
    private String nickname;       // 참가자 닉네임 (name 대신 nickname 사용)
    private ParticipationStatus status; // 참가 상태 (대기중, 수락됨, 거절됨 등)
}
