package com.book.BookMeetingWeb.controller;

import com.book.BookMeetingWeb.service.MeetingMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/meetings")
@RequiredArgsConstructor
public class MeetingMemberController {

    private final MeetingMemberService meetingMemberService;

    @PostMapping("/{meetingId}/join")
    public ResponseEntity<String> joinMeeting(@PathVariable Long meetingId,
                                              @RequestParam Long userId) {
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
