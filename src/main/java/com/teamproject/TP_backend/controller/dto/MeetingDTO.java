package com.teamproject.TP_backend.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

// 클라이언트와 데이터를 주고받기 위한 모임(Meeting) DTO 클래스
// 모임 생성, 조회, 수정 시 사용

@Data
@Builder
@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") // ISO 8601 형식으로 날짜 직렬화
public class MeetingDTO {

    private Long id; // 모임 고유 ID (PK)

    @NotBlank(message = "모임명은 필수입니다.")
    private String title; // 모임명

    private String description; // 모임 소개글

    private String bookTitle; // 책 제목

    private String bookAuthor; // 책 저자

    private String bookCover; // 책 표지 이미지 URL

    private String bookCategory; // 책 장르 또는 카테고리

    private LocalDateTime startDate; // 모임 시작 일시

    private int maxMembers; // 최대 참여 가능 인원 수

    private boolean active; // 모임 활성화 여부 (false면 삭제됨으로 간주)

    private List<ParticipantDTO> participants; // 참여자 목록 DTO 리스트

    private Long hostId; // 모임을 생성한 호스트의 사용자 ID

    private String hostNickname; // 모임 호스트의 닉네임 (프론트 노출용)

    private String hostEmail; // 모임 호스트의 이메일 (프론트 노출용)

    private String channelUrl; // Sendbird 채널 URL (프론트에서 채팅 입장에 사용)

}
