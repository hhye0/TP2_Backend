package com.teamproject.TP_backend.service;

import com.teamproject.TP_backend.controller.dto.MeetingDTO;
import com.teamproject.TP_backend.domain.entity.Meeting;
import com.teamproject.TP_backend.domain.entity.User;
import com.teamproject.TP_backend.repository.MeetingRepository;
import com.teamproject.TP_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

// 모임(Meeting) 관련 비즈니스 로직을 처리하는 서비스 클래스
// - 모임 생성, 조회, 수정, 삭제 등의 기능을 담당
@Service
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository; // 사용자 정보 조회용 (호스트 설정 등)

    //     전체 모임 리스트 조회
    //     @return 모든 모임의 MeetingDTO 리스트
    public List<MeetingDTO> getAllMeetings() {
        return meetingRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    //     단일 모임 조회
    //     @param id 조회할 모임 ID
    //     @return 해당 모임의 DTO
    //     @throws RuntimeException 모임이 없을 경우
    public MeetingDTO getMeeting(Long id) {
        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("모임을 찾을 수 없습니다."));
        return toDTO(meeting);
    }

    //     모임 생성
    //     @param dto 생성할 모임 정보 DTO
    //     @param user 현재 로그인한 사용자 (호스트)
    //     @return 생성된 모임의 DTO
    public MeetingDTO createMeeting(MeetingDTO dto, User user) {
        Meeting meeting = Meeting.builder()
                .title(dto.getTitle())
                .host(user) // 현재 로그인한 유저를 호스트로 설정
                .bookTitle(dto.getBookTitle())
                .bookAuthor(dto.getBookAuthor())
                .bookCover(dto.getBookCover())
                .bookCategory(dto.getBookCategory())
                .startDate(dto.getStartDate())
                .maxMembers(dto.getMaxMembers())
                .description(dto.getDescription())         // 모임 소개글 추가
                .isActive(true)                            // 생성 시 활성화 상태
                .build();

        Meeting saved = meetingRepository.save(meeting);
        return toDTO(saved); // Entity → DTO 변환 후 반환
    }

    //     모임 수정
    //     @param id 수정 대상 모임 ID
    //     @param dto 수정할 정보가 담긴 DTO
    //     @param user 로그인한 사용자 (호스트 검증용)
    //     @return 수정된 모임의 DTO
    //     @throws RuntimeException 권한 없거나 모임이 없을 경우
    public MeetingDTO updateMeeting(Long id, MeetingDTO dto, User user) {
        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("모임을 찾을 수 없습니다."));

        // 권한 검사: 로그인한 사용자와 모임 호스트가 일치해야 수정 가능
        if (!meeting.getHost().getId().equals(user.getId())) {
            throw new RuntimeException("권한이 없습니다.");
        }

        // 필드 값 업데이트
        meeting.setTitle(dto.getTitle());
        meeting.setBookTitle(dto.getBookTitle());
        meeting.setBookAuthor(dto.getBookAuthor());
        meeting.setBookCover(dto.getBookCover());
        meeting.setBookCategory(dto.getBookCategory());
        meeting.setStartDate(dto.getStartDate());
        meeting.setMaxMembers(dto.getMaxMembers());
        meeting.setDescription(dto.getDescription());     // 모임 소개글 업데이트

        meeting.setActive(dto.isActive()); // 모집상태(모집중/마감)도 업데이트 가능!

        Meeting updated = meetingRepository.save(meeting);
        return toDTO(updated);
    }

    public void updateMeetingStatus(Long id, boolean active, User user) {
        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("모임을 찾을 수 없습니다."));
        if (!meeting.getHost().getId().equals(user.getId())) {
            throw new RuntimeException("모임 상태를 변경할 권한이 없습니다.");
        }
        meeting.setActive(active);
        meetingRepository.save(meeting);
    }


    //     모임 삭제
    //     @param id 삭제할 모임 ID
    //     @param user 현재 로그인한 사용자 (권한 체크)
    public void deleteMeeting(Long id, User user) {
        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("모임을 찾을 수 없습니다."));

        if (!meeting.getHost().getId().equals(user.getId())) {
            throw new RuntimeException("모임을 삭제할 권한이 없습니다.");
        }

        meetingRepository.delete(meeting); // 안전하게 삭제
    }

    // Meeting 엔티티 → MeetingDTO로 변환
    // @param meeting 변환 대상 엔티티
    private MeetingDTO toDTO(Meeting meeting) {
        return MeetingDTO.builder()
                .id(meeting.getId())
                .title(meeting.getTitle())
                .bookTitle(meeting.getBookTitle())
                .bookAuthor(meeting.getBookAuthor())
                .bookCover(meeting.getBookCover())
                .bookCategory(meeting.getBookCategory())
                .startDate(meeting.getStartDate())
                .maxMembers(meeting.getMaxMembers())
                .active(meeting.isActive())
                .hostId(meeting.getHost().getId())               // 호스트 ID
                .hostEmail(meeting.getHost().getEmail())         // 호스트 이메일
                .description(meeting.getDescription())           // 모임 소개글
                .channelUrl(meeting.getChannelUrl())             // Sendbird 채널 URL (프론트에서 채팅 입장에 필요)
                .build();
    }
}