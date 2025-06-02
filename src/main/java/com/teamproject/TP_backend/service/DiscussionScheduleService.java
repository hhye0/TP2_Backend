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

    public DiscussionScheduleDTO createSchedule(Long meetingId, DiscussionScheduleDTO dto) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new RuntimeException("모임을 찾을 수 없습니다."));

        DiscussionSchedule schedule = DiscussionSchedule.builder()
                .meeting(meeting)
                .title(dto.getTitle())
                .dateTime(dto.getDateTime())
                .content(dto.getContent())
                .build();

        return toDTO(scheduleRepository.save(schedule));
    }

    private DiscussionScheduleDTO toDTO(DiscussionSchedule s) {
        return DiscussionScheduleDTO.builder()
                .id(s.getId())
                .title(s.getTitle())
                .dateTime(s.getDateTime())
                .content(s.getContent())
                .build();
    }
}
