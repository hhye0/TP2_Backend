package com.teamproject.TP_backend.controller;

import com.teamproject.TP_backend.config.CurrentUser;
import com.teamproject.TP_backend.config.security.CustomUserDetails;
import com.teamproject.TP_backend.controller.dto.DiscussionScheduleDTO;
import com.teamproject.TP_backend.domain.entity.User;
import com.teamproject.TP_backend.service.DiscussionScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 모임(Meeting)에 속한 "토론 일정"을 생성, 수정, 삭제, 조회하는 기능을 제공
// 주로 독서 모임에서 참가자 간 토론 일정을 등록, 관리하는 데 사용
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/meetings")
public class DiscussionScheduleController {

    private final DiscussionScheduleService discussionScheduleService;

    // [GET] 특정 모임의 토론 일정 전체 조회 (모든 참여자 조회 가능)
    @GetMapping("/{meetingId}/schedules")
    public ResponseEntity<List<DiscussionScheduleDTO>> getSchedules(@PathVariable Long meetingId) {
        return ResponseEntity.ok(discussionScheduleService.getSchedulesByMeeting(meetingId));
    }

    // [GET] 특정 일정 상세 조회
    @GetMapping("/{scheduleId}")
    public ResponseEntity<DiscussionScheduleDTO> getDiscussion(@PathVariable Long id) {
        return ResponseEntity.ok(discussionScheduleService.getById(id));
    }

    // [POST] 토론 일정 생성 - 호스트만 가능
    @PostMapping("/{meetingId}/schedules")
    public ResponseEntity<DiscussionScheduleDTO> createSchedule(
            @PathVariable Long meetingId,
            @RequestBody DiscussionScheduleDTO dto,
            @CurrentUser User user) {

        Long userId = user.getId();
        return ResponseEntity.ok(discussionScheduleService.createSchedule(meetingId, dto, userId));
    }

    // [PUT] 토론 일정 수정 - 호스트만 가능
    @PutMapping("/schedules/{scheduleId}")
    public ResponseEntity<DiscussionScheduleDTO> updateSchedule(
            @PathVariable Long scheduleId,
            @RequestBody DiscussionScheduleDTO dto,
            @CurrentUser CustomUserDetails userDetails) {

        Long userId = userDetails.getUser().getId();
        return ResponseEntity.ok(discussionScheduleService.updateSchedule(scheduleId, dto, userId));
    }

    // [DELETE] 토론 일정 삭제 - 호스트만 가능
    @DeleteMapping("/schedules/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(
            @PathVariable Long scheduleId,
            @CurrentUser CustomUserDetails userDetails) {

        Long userId = userDetails.getUser().getId();
        discussionScheduleService.deleteSchedule(scheduleId, userId);
        return ResponseEntity.noContent().build();
    }

}
