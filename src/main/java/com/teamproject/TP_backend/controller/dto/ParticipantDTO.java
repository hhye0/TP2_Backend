package com.teamproject.TP_backend.controller.dto;

import com.teamproject.TP_backend.domain.enums.ParticipantStatus;
import lombok.Builder;
import lombok.Data;


// 모임 참가자 정보를 담는 DTO
// 모임 조회 시 참가자 목록에 포함되어 전달

@Data
@Builder
public class ParticipantDTO {
    private Long userId;           // 참가자 사용자 ID
    private String userName;       // 참가자 이름
    private ParticipantStatus status; // 참가 상태 (대기중, 수락됨, 거절됨 등)
}
