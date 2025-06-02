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

import java.util.List;

// 모임 참여(신청 및 승인/거절) 관련 비즈니스 로직을 처리하는 서비스 클래스
@Service
@RequiredArgsConstructor
public class MeetingMemberService {

    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;
    private final MeetingMemberRepository meetingMemberRepository;
    private final ChatService chatService;

    //     사용자가 특정 모임에 참여 신청하는 기능
//     @param meetingId 신청할 모임 ID
//     @param userId 신청하는 사용자 ID
//     @throws RuntimeException 모임이나 사용자 없을 때, 이미 신청했을 때
    public void joinMeeting(Long meetingId, Long userId) {
        // 모임 조회
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new RuntimeException("모임이 존재하지 않습니다."));

        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다."));

        // 중복 신청 방지
        boolean alreadyJoined = meetingMemberRepository.existsByMeetingAndUser(meeting, user);
        if (alreadyJoined) {
            throw new RuntimeException("이미 참여한 모임입니다.");
        }

        // 모임 참여 정보 생성 및 저장
        MeetingMember member = MeetingMember.create(meeting, user);
        meetingMemberRepository.save(member);
    }

    //     호스트가 참여 요청에 대해 수락/거절 응답 처리
    //     @param meetingId 모임 ID
    //     @param userId 응답할 사용자 ID
    //     @param approve true면 승인, false면 거절
    //     @throws RuntimeException 신청 정보 없을 때
    @Transactional
    public void respondToParticipation(Long meetingId, Long userId, boolean approve) {
        // 참여 요청 조회
        MeetingMember member = meetingMemberRepository.findByMeetingIdAndUserId(meetingId, userId)
                .orElseThrow(() -> new RuntimeException("신청 정보를 찾을 수 없습니다."));

        if (approve) {
            // 중복 승인 방지
            if (member.getStatus() != MeetingMember.ParticipationStatus.APPROVED) {
                member.setStatus(MeetingMember.ParticipationStatus.APPROVED);

                // 채널 초대
                Meeting meeting = member.getMeeting();
                String channelUrl = meeting.getChannelUrl();
                String sendbirdUserId = String.valueOf(userId); // Sendbird ID는 userId 기준

                chatService.inviteUser(channelUrl, List.of(sendbirdUserId));
            }
        } else {
            member.setStatus(MeetingMember.ParticipationStatus.REJECTED);
        }
    }

    // 사용자가 모임을 탈퇴하는 기능
    // DB에서 참여 정보를 삭제하고, 채팅에서도 퇴장시킴
    @Transactional
    public void leaveMeeting(Long meetingId, Long userId) {
        // 해당 모임과 사용자 조합으로 참여 정보 조회
        MeetingMember member = meetingMemberRepository.findByMeetingIdAndUserId(meetingId, userId)
                .orElseThrow(() -> new RuntimeException("참여 기록이 없습니다."));

        // DB에서 참여 정보 삭제
        meetingMemberRepository.delete(member);

        // 채팅에서 퇴장 처리
        String channelUrl = member.getMeeting().getChannelUrl(); // 모임의 채팅 채널 URL
        chatService.leaveChannel(channelUrl, String.valueOf(userId)); // Sendbird API 호출
    }

}
