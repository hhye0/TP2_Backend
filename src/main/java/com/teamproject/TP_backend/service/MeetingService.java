package com.teamproject.TP_backend.service;

import com.teamproject.TP_backend.controller.dto.MeetingDTO;
import com.teamproject.TP_backend.entity.Meeting;
import com.teamproject.TP_backend.entity.User;
import com.teamproject.TP_backend.repository.MeetingRepository;
import com.teamproject.TP_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository; // ✅ 추가

    public List<Meeting> getAllMeetings() {
        return meetingRepository.findAll();
    }

    public Meeting getMeeting(Long id) {
        return meetingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("모임을 찾을 수 없습니다."));
    }

    public Meeting createMeeting(MeetingDTO dto) {
        // ✅ 1. hostId를 DTO에서 가져온다고 가정
        User host = userRepository.findById(dto.getHostId())
                .orElseThrow(() -> new RuntimeException("호스트 유저를 찾을 수 없습니다."));

        // ✅ 2. host 설정 포함
        Meeting meeting = Meeting.builder()
                .title(dto.getTitle())
                .bookTitle(dto.getBookTitle())
                .bookAuthor(dto.getBookAuthor())
                .bookCover(dto.getBookCover())
                .bookCategory(dto.getBookCategory())
                .startDate(dto.getStartDate())
                .maxMembers(dto.getMaxMembers())
                .isActive(true)
                .host(host) // ✅ 핵심
                .build();

        return meetingRepository.save(meeting);
    }

    public Meeting updateMeeting(Long id, MeetingDTO dto) {
        Meeting meeting = getMeeting(id);
        meeting.setTitle(dto.getTitle());
        meeting.setBookTitle(dto.getBookTitle());
        meeting.setBookAuthor(dto.getBookAuthor());
        meeting.setBookCover(dto.getBookCover());
        meeting.setBookCategory(dto.getBookCategory());
        meeting.setStartDate(dto.getStartDate());
        meeting.setMaxMembers(dto.getMaxMembers());

        // 필요하면 host 변경도 추가 가능
        return meetingRepository.save(meeting);
    }

    public void deleteMeeting(Long id) {
        meetingRepository.deleteById(id);
    }
}
