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

@Service
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository; // 추가

    public List<MeetingDTO> getAllMeetings() {
        return meetingRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }


    public MeetingDTO getMeeting(Long id) {
        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("모임을 찾을 수 없습니다."));
        return toDTO(meeting);
    }

    public MeetingDTO createMeeting(MeetingDTO dto, User user) {
        // 2. host 설정 포함
        Meeting meeting = Meeting.builder()
                .title(dto.getTitle())
                .host(user)
                .bookTitle(dto.getBookTitle())
                .bookAuthor(dto.getBookAuthor())
                .bookCover(dto.getBookCover())
                .bookCategory(dto.getBookCategory())
                .startDate(dto.getStartDate())
                .maxMembers(dto.getMaxMembers())
                .isActive(true)
                .build();

        Meeting saved = meetingRepository.save(meeting);
        return toDTO(saved);
    }

    public MeetingDTO updateMeeting(Long id, MeetingDTO dto, User user) {
        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("모임을 찾을 수 없습니다."));

        // host가 로그인한 user와 동일한지 체크 (권한 검사)
        if (!meeting.getHost().getId().equals(user.getId())) {
            throw new RuntimeException("권한이 없습니다.");
        }
        meeting.setTitle(dto.getTitle());
        meeting.setBookTitle(dto.getBookTitle());
        meeting.setBookAuthor(dto.getBookAuthor());
        meeting.setBookCover(dto.getBookCover());
        meeting.setBookCategory(dto.getBookCategory());
        meeting.setStartDate(dto.getStartDate());
        meeting.setMaxMembers(dto.getMaxMembers());

        Meeting updated = meetingRepository.save(meeting);
        return toDTO(updated);
    }

    public void deleteMeeting(Long id, User user) {
        meetingRepository.deleteById(id);
    }

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
                .hostId(meeting.getHost().getId())
                .build();
    }
}
