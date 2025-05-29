package com.teamproject.TP_backend.controller;

import com.teamproject.TP_backend.config.CurrentUser;
import com.teamproject.TP_backend.config.security.CustomUserDetails;
import com.teamproject.TP_backend.service.MeetingMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/meetings")
@RequiredArgsConstructor
public class MeetingMemberController {

    private final MeetingMemberService meetingMemberService;

    @PostMapping("/{meetingId}/join")
    public ResponseEntity<String> joinMeeting(
            @PathVariable Long meetingId,
            @CurrentUser CustomUserDetails userDetails) {
        Long userId = userDetails.getUser().getId();
        meetingMemberService.joinMeeting(meetingId, userId);
        return ResponseEntity.ok("참여 신청이 완료되었습니다.");
    }


    @PostMapping("/{meetingId}/respond")
    public ResponseEntity<Void> respondToJoin(
            @PathVariable Long meetingId,
            @RequestParam Long userId,
            @RequestParam boolean approve) {

        meetingMemberService.respondToParticipation(meetingId, userId, approve);
        return ResponseEntity.ok().build();
    }
}
