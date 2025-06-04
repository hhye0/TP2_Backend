package com.teamproject.TP_backend.service;

import com.teamproject.TP_backend.controller.dto.DiscussionScheduleDTO;
import com.teamproject.TP_backend.domain.entity.DiscussionSchedule;
import com.teamproject.TP_backend.domain.entity.Meeting;
import com.teamproject.TP_backend.domain.enums.GroupRole;
import com.teamproject.TP_backend.exception.PastDateScheduleException;
import com.teamproject.TP_backend.repository.DiscussionScheduleRepository;
import com.teamproject.TP_backend.repository.MeetingMemberRepository;
import com.teamproject.TP_backend.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

// 토론 일정 비지니스 로직 처리
@Service
@RequiredArgsConstructor
public class DiscussionScheduleService {

    private final DiscussionScheduleRepository scheduleRepository;
    private final MeetingRepository meetingRepository;
    private final MeetingMemberRepository meetingMemberRepository;

    // 모임의 모든 일정 조회
    public List<DiscussionScheduleDTO> getSchedulesByMeeting(Long meetingId) {
        return scheduleRepository.findByMeetingId(meetingId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // 단일 일정 상세 조회
    public DiscussionScheduleDTO getById(Long id) {
        DiscussionSchedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("일정을 찾을 수 없습니다."));
        return DiscussionScheduleDTO.fromEntity(schedule);
    }

    // 일정 생성 - 호스트만 가능
    public DiscussionScheduleDTO createSchedule(Long meetingId, DiscussionScheduleDTO dto, Long userId) {
        validateHost(meetingId, userId);

        // 과거 날짜로는 생성 불가
        if (dto.date().isBefore(LocalDate.now())) {
            throw new PastDateScheduleException("과거 날짜로는 일정을 생성할 수 없습니다.");
        }

        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new RuntimeException("모임을 찾을 수 없습니다."));

        DiscussionSchedule schedule = DiscussionSchedule.builder()
                .meeting(meeting)
                .topic(dto.topic())
                .date(dto.date())
                .time(dto.time()) // 추가
                .memo(dto.memo())
                .meeting(meetingRepository.findById(meetingId)
                        .orElseThrow(() -> new IllegalArgumentException("모임이 존재하지 않습니다.")))
                .build();

        scheduleRepository.save(schedule);
        return DiscussionScheduleDTO.fromEntity(schedule);
    }

    // 일정 수정 - 호스트만 가능
    public DiscussionScheduleDTO updateSchedule(Long scheduleId, DiscussionScheduleDTO dto, Long userId) {
        DiscussionSchedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("토론 일정을 찾을 수 없습니다."));

        validateHost(schedule.getMeeting().getId(), userId);

        schedule.setTopic(dto.topic());
        schedule.setDate(dto.date());
        schedule.setTime(dto.time()); // 추가
        schedule.setMemo(dto.memo());

        return toDTO(scheduleRepository.save(schedule));
    }

    // 일정 삭제 - 호스트만 가능
    public void deleteSchedule(Long scheduleId, Long userId) {
        DiscussionSchedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("토론 일정을 찾을 수 없습니다."));

        validateHost(schedule.getMeeting().getId(), userId);

        scheduleRepository.delete(schedule);
    }

    // 호스트 권한 검사 메서드
    private void validateHost(Long meetingId, Long userId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new RuntimeException("모임이 존재하지 않습니다."));

        if (!meeting.getHost().getId().equals(userId)) {
            throw new AccessDeniedException("호스트만 이 작업을 할 수 있습니다.");
        }
    }


    // DTO 변환 도구
    private DiscussionScheduleDTO toDTO(DiscussionSchedule s) {
        return new DiscussionScheduleDTO(
                s.getId(),
                s.getTopic(),
                s.getDate(),
                s.getTime(),
                s.getMemo(),
                s.getMeeting().getId()
        );
    }
}
