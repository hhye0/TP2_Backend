package com.teamproject.TP_backend.controller;

import com.teamproject.TP_backend.config.CurrentUser;
import com.teamproject.TP_backend.config.security.CustomUserDetails;
import com.teamproject.TP_backend.domain.entity.User;
import com.teamproject.TP_backend.service.MeetingMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

// 모임 참여 신청 및 승인/거절 처리를 담당하는 컨트롤러
// 경로: /api/meetings
@RestController
@RequestMapping("/api/meetings")
@RequiredArgsConstructor
public class MeetingMemberController {

    private final MeetingMemberService meetingMemberService;

    //     모임 참여 신청 API
    //     - 로그인한 사용자가 특정 모임에 참여를 요청
    //     @param meetingId 신청할 모임 ID
    //     @param userDetails 현재 로그인한 사용자 정보
    @PostMapping("/{meetingId}/join")
    public ResponseEntity<Map<String, String>> joinMeeting(
            @PathVariable Long meetingId,
            @CurrentUser User user) {
        try {
            meetingMemberService.joinMeeting(meetingId, user.getId());
            return ResponseEntity.ok(Map.of("message", "참여 신청이 완료되었습니다."));
        } catch (RuntimeException e) {
            Map<String, String> errorBody = Map.of("error", e.getMessage());
            return ResponseEntity.<Map<String, String>>badRequest().body(errorBody);
        }
    }

    //     모임 참여 수락/거절 응답 API
    //     - 호스트가 특정 유저의 참여 요청에 대해 승인 또는 거절
    //     @param meetingId 모임 ID
    //     @param userId 참여 요청한 사용자 ID
    //     @param approve true: 수락, false: 거절
    //     @return 200 OK 응답
    @PostMapping("/{meetingId}/respond")
    public ResponseEntity<Void> respondToJoin(
            @PathVariable Long meetingId,
            @RequestParam Long userId,
            @RequestParam boolean approve) {

        meetingMemberService.respondToParticipation(meetingId, userId, approve);
        return ResponseEntity.ok().build();
    }

    // 모임 탈퇴 API
    // - 사용자가 모임을 나가면 해당 채팅방에서도 자동으로 퇴장됨
    @DeleteMapping("/{meetingId}/leave")
    public ResponseEntity<Void> leaveMeeting(
            @PathVariable Long meetingId,
            @CurrentUser User user) {
        meetingMemberService.leaveMeeting(meetingId, user.getId()); // 서비스 호출
        return ResponseEntity.ok().build(); // 성공 응답 반환
    }
}
