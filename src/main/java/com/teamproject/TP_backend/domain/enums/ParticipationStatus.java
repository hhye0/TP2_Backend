package com.teamproject.TP_backend.domain.enums;

// 모임 참가 신청 상태를 나타내는 열거형(Enum)
// - 각 참가자의 현재 상태를 표현할 때 사용
public enum ParticipationStatus {

    PENDING,   // 신청 대기 중
    APPROVED,  // 호스트에 의해 수락됨
    REJECTED   // 호스트에 의해 거절됨
}
