package com.teamproject.TP_backend.service;

import com.teamproject.TP_backend.controller.dto.MeetingDTO;
import com.teamproject.TP_backend.entity.Meeting;
import com.teamproject.TP_backend.entity.User;
import com.teamproject.TP_backend.repository.MeetingRepository;
import com.teamproject.TP_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository; //✅

    public List<MeetingDTO> getAllMeetings() {
        return meetingRepository.findAll().stream()
                .map(meeting -> MeetingDTO.builder()
                        .id(meeting.getId())
                        .title(meeting.getTitle())
                        .bookTitle(meeting.getBookTitle())
                        .bookAuthor(meeting.getBookAuthor())
                        .bookCover(meeting.getBookCover())
                        .bookCategory(meeting.getBookCategory())
                        .startDate(meeting.getStartDate())
                        .maxMembers(meeting.getMaxMembers())
                        .active(meeting.isActive())
                        .hostName(meeting.getHost().getName())  // ✅️host 객체 대신 이름만 추출
                        .build())
                .toList();
    }

    public Meeting getMeeting(Long id) {
        return meetingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("모임을 찾을 수 없습니다."));
    }

    public Meeting createMeeting(MeetingDTO dto) {
        //현재 로그인된 사용자 정보 -> host 로 저장
        String email = SecurityContextHolder.getContext().getAuthentication().getName(); //✅
        User host = userRepository.findByEmail(email) //✅
                .orElseThrow(() -> new RuntimeException("로그인된 사용자를 찾을 수 없습니다.")); //✅
        Meeting meeting = Meeting.builder()
                .title(dto.getTitle())
                .bookTitle(dto.getBookTitle())
                .bookAuthor(dto.getBookAuthor())
                .bookCover(dto.getBookCover())
                .bookCategory(dto.getBookCategory())
                .startDate(dto.getStartDate())
                .maxMembers(dto.getMaxMembers())
                .isActive(true)
                .host(host) //✅
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
        return meetingRepository.save(meeting);
    }

    public void deleteMeeting(Long id) {
        meetingRepository.deleteById(id);
    }
}
