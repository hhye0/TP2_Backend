package com.book.BookMeetingWeb.service;

import com.book.BookMeetingWeb.controller.dto.MeetingDTO;
import com.book.BookMeetingWeb.entity.Meeting;
import com.book.BookMeetingWeb.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;

    public List<Meeting> getAllMeetings() {
        return meetingRepository.findAll();
    }

    public Meeting getMeeting(Long id) {
        return meetingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("모임을 찾을 수 없습니다."));
    }

    public Meeting createMeeting(MeetingDTO dto) {
        Meeting meeting = Meeting.builder()
                .title(dto.getTitle())
                .bookTitle(dto.getBookTitle())
                .bookAuthor(dto.getBookAuthor())
                .bookCover(dto.getBookCover())
                .bookCategory(dto.getBookCategory())
                .startDate(dto.getStartDate().atStartOfDay())
                .endDate(dto.getEndDate().atStartOfDay())
                .maxMembers(dto.getMaxMembers())
                .isActive(true)
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
        meeting.setStartDate(dto.getStartDate().atStartOfDay());
        meeting.setEndDate(dto.getEndDate().atStartOfDay());
        meeting.setMaxMembers(dto.getMaxMembers());
        return meetingRepository.save(meeting);
    }

    public void deleteMeeting(Long id) {
        meetingRepository.deleteById(id);
    }
}
