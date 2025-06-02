package com.teamproject.TP_backend.controller;

import com.teamproject.TP_backend.controller.dto.DiscussionScheduleDTO;
import com.teamproject.TP_backend.service.DiscussionScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/meetings")
public class DiscussionScheduleController {

    private final DiscussionScheduleService discussionScheduleService;

    @GetMapping("/{meetingId}/schedules")
    public ResponseEntity<List<DiscussionScheduleDTO>> getSchedules(@PathVariable Long meetingId) {
        return ResponseEntity.ok(discussionScheduleService.getSchedulesByMeeting(meetingId));
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<DiscussionScheduleDTO> getDiscussion(@PathVariable Long id) {
        return ResponseEntity.ok(discussionScheduleService.getById(id));
    }

    @PostMapping("/{meetingId}/schedules")
    public ResponseEntity<DiscussionScheduleDTO> createSchedule(
            @PathVariable Long meetingId,
            @RequestBody DiscussionScheduleDTO dto
    ) {
        return ResponseEntity.ok(discussionScheduleService.createSchedule(meetingId, dto));
    }

    @PutMapping("/schedules/{scheduleId}")
    public ResponseEntity<DiscussionScheduleDTO> updateSchedule(
            @PathVariable Long scheduleId,
            @RequestBody DiscussionScheduleDTO dto
    ) {
        return ResponseEntity.ok(discussionScheduleService.updateSchedule(scheduleId, dto));
    }

    @DeleteMapping("/schedules/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long scheduleId) {
        discussionScheduleService.deleteSchedule(scheduleId);
        return ResponseEntity.noContent().build();
    }

}
