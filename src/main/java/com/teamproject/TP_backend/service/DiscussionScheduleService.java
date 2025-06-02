package com.teamproject.TP_backend.service;

import com.teamproject.TP_backend.controller.dto.DiscussionScheduleDTO;
import com.teamproject.TP_backend.domain.entity.DiscussionSchedule;
import com.teamproject.TP_backend.domain.entity.Meeting;
import com.teamproject.TP_backend.repository.DiscussionScheduleRepository;
import com.teamproject.TP_backend.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscussionScheduleService {

    private final DiscussionScheduleRepository scheduleRepository;
    private final MeetingRepository meetingRepository;

    public List<DiscussionScheduleDTO> getSchedulesByMeeting(Long meetingId) {
        return scheduleRepository.findByMeetingId(meetingId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public DiscussionScheduleDTO getById(Long id) {
        DiscussionSchedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("일정을 찾을 수 없습니다."));
        return DiscussionScheduleDTO.fromEntity(schedule);
    }

    public DiscussionScheduleDTO createSchedule(Long meetingId, DiscussionScheduleDTO dto) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new RuntimeException("모임을 찾을 수 없습니다."));

        DiscussionSchedule schedule = DiscussionSchedule.builder()
                .meeting(meeting)
                .topic(dto.topic())
                .date(dto.date())
                .time(dto.time()) // ✅ 추가
                .memo(dto.memo())
                .build();

        return toDTO(scheduleRepository.save(schedule));
    }

    public DiscussionScheduleDTO updateSchedule(Long scheduleId, DiscussionScheduleDTO dto) {
        DiscussionSchedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("토론 일정을 찾을 수 없습니다."));

        schedule.setTopic(dto.topic());
        schedule.setDate(dto.date());
        schedule.setTime(dto.time()); // 추가
        schedule.setMemo(dto.memo());

        return toDTO(scheduleRepository.save(schedule));
    }

    public void deleteSchedule(Long scheduleId) {
        DiscussionSchedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("토론 일정을 찾을 수 없습니다."));

        scheduleRepository.delete(schedule);
    }

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
