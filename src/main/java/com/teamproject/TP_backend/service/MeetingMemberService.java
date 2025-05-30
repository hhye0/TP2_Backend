package com.teamproject.TP_backend.service;

import com.teamproject.TP_backend.domain.entity.Meeting;
import com.teamproject.TP_backend.domain.entity.MeetingMember;
import com.teamproject.TP_backend.domain.entity.User;
import com.teamproject.TP_backend.repository.MeetingMemberRepository;
import com.teamproject.TP_backend.repository.MeetingRepository;
import com.teamproject.TP_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MeetingMemberService {

    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;
    private final MeetingMemberRepository meetingMemberRepository;

    public void joinMeeting(Long meetingId, Long userId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new RuntimeException("모임이 존재하지 않습니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다."));

        boolean alreadyJoined = meetingMemberRepository.existsByMeetingAndUser(meeting, user);
        if (alreadyJoined) {
            throw new RuntimeException("이미 참여한 모임입니다.");
        }

        MeetingMember member = MeetingMember.create(meeting, user);
        meetingMemberRepository.save(member);
    }

    @Transactional
    public void respondToParticipation(Long meetingId, Long userId, boolean approve) {
        MeetingMember member = meetingMemberRepository.findByMeetingIdAndUserId(meetingId, userId)
                .orElseThrow(() -> new RuntimeException("신청 정보를 찾을 수 없습니다."));

        if (approve) {
            member.setStatus(MeetingMember.ParticipationStatus.APPROVED);
        } else {
            member.setStatus(MeetingMember.ParticipationStatus.REJECTED);
        }
    }
}