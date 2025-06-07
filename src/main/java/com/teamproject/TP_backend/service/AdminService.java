package com.teamproject.TP_backend.service;

import com.teamproject.TP_backend.controller.dto.MeetingDTO;
import com.teamproject.TP_backend.domain.entity.Meeting;
import com.teamproject.TP_backend.domain.entity.User;
import com.teamproject.TP_backend.domain.enums.UserRole;
import com.teamproject.TP_backend.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final MeetingRepository meetingRepository;

    // 모든 모임 조회 (관리자 전용)
    public List<MeetingDTO> getAllMeetings(User user) {
        if (user.getRole() != UserRole.ADMIN) {
            throw new AccessDeniedException("관리자 권한이 필요합니다.");
        }

        List<Meeting> meetings = meetingRepository.findAll();

        // Entity → DTO 변환
        return meetings.stream()
                .map(MeetingDTO::from)
                .collect(Collectors.toList());
    }

    // 모임 삭제
    public void deleteMeeting(Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new IllegalArgumentException("해당 모임이 존재하지 않습니다."));
        meetingRepository.delete(meeting);
    }

    // 채팅 ON/OFF 토글
    public void turnOnChat(Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new IllegalArgumentException("해당 모임이 존재하지 않습니다."));
        meeting.setChatEnabled(true);
        meetingRepository.save(meeting);
    }

    public void turnOffChat(Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new IllegalArgumentException("해당 모임이 존재하지 않습니다."));
        meeting.setChatEnabled(false);
        meetingRepository.save(meeting);
    }

}
