package com.teamproject.TP_backend.controller;

import com.teamproject.TP_backend.config.CurrentUser;
import com.teamproject.TP_backend.controller.dto.MeetingDTO;

import com.teamproject.TP_backend.domain.entity.User;
import com.teamproject.TP_backend.service.MeetingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meetings")
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingService meetingService;

    @GetMapping
    public ResponseEntity<List<MeetingDTO>> getAllMeetings() {
        return ResponseEntity.ok(meetingService.getAllMeetings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MeetingDTO> getMeeting(@PathVariable Long id) {
        return ResponseEntity.ok(meetingService.getMeeting(id));
    }

    @PostMapping
    public ResponseEntity<MeetingDTO> createMeeting(@RequestBody @Valid MeetingDTO dto,
                                                    @CurrentUser User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(meetingService.createMeeting(dto, user));
    }


    @PutMapping("/{id}")
    public ResponseEntity<MeetingDTO> updateMeeting(@PathVariable Long id, @RequestBody @Valid MeetingDTO dto, @CurrentUser User user) {
        return ResponseEntity.ok(meetingService.updateMeeting(id, dto, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMeeting(@PathVariable Long id, @CurrentUser User user) {
        meetingService.deleteMeeting(id, user);
        return ResponseEntity.noContent().build();
    }
}
