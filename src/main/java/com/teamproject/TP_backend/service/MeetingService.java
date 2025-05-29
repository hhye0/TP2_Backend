package com.teamproject.TP_backend.service;

import com.teamproject.TP_backend.controller.dto.MeetingDTO;
import com.teamproject.TP_backend.domain.entity.Meeting;
import com.teamproject.TP_backend.domain.entity.User;
import com.teamproject.TP_backend.repository.MeetingRepository;
import com.teamproject.TP_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public MeetingDTO createMeeting(MeetingDTO dto, org.springframework.security.core.userdetails.User user) {
        //현재 로그인된 사용자 정보 -> host 로 저장
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User host = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("로그인된 사용자를 찾을 수 없습니다."));
        // 2. host 설정 포함
        Meeting meeting = Meeting.builder()
                .title(dto.getTitle())
                .bookTitle(dto.getBookTitle())
                .bookAuthor(dto.getBookAuthor())
                .bookCover(dto.getBookCover())
                .bookCategory(dto.getBookCategory())
                .startDate(dto.getStartDate())
                .maxMembers(dto.getMaxMembers())
                .isActive(true)
                .host(host)
                .build();

        Meeting saved = meetingRepository.save(meeting);
        return toDTO(saved);
    }

    public MeetingDTO updateMeeting(Long id, MeetingDTO dto, org.springframework.security.core.userdetails.User user) {
        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("모임을 찾을 수 없습니다."));

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

    public void deleteMeeting(Long id, org.springframework.security.core.userdetails.User user) {
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
