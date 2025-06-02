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

// 모임(Meeting) 관련 API를 처리하는 컨트롤러
// 생성, 조회, 수정, 삭제 기능을 제공
// 경로: /api/meetings
@RestController
@RequestMapping("/api/meetings")
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingService meetingService;

    //     전체 모임 목록 조회 API
    //     @return 모든 모임 리스트
    @GetMapping
    public ResponseEntity<List<MeetingDTO>> getAllMeetings() {
        return ResponseEntity.ok(meetingService.getAllMeetings());
    }

    //     단일 모임 상세 조회 API
    //     @param id 조회할 모임의 ID
    //     @return 해당 모임의 상세 정보
    @GetMapping("/{id}")
    public ResponseEntity<MeetingDTO> getMeeting(@PathVariable Long id) {
        return ResponseEntity.ok(meetingService.getMeeting(id));
    }

    //     모임 생성 API
    //     @param dto 생성할 모임 정보
    //     @param user 현재 로그인한 사용자 (호스트로 설정됨)
    //     @return 생성된 모임 정보와 201 Created 응답
    @PostMapping
    public ResponseEntity<MeetingDTO> createMeeting(@RequestBody @Valid MeetingDTO dto,
                                                    @CurrentUser User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(meetingService.createMeeting(dto, user));
    }

    //     모임 수정 API
    //     @param id 수정할 모임 ID
    //     @param dto 수정할 내용
    //     @param user 현재 로그인한 사용자 (호스트 권한 확인용)
    //     @return 수정된 모임 정보
    @PutMapping("/{id}")
    public ResponseEntity<MeetingDTO> updateMeeting(@PathVariable Long id,
                                                    @RequestBody @Valid MeetingDTO dto,
                                                    @CurrentUser User user) {
        return ResponseEntity.ok(meetingService.updateMeeting(id, dto, user));
    }

    @PatchMapping("/{meetingId}/status")
    public ResponseEntity<String> updateMeetingStatus(
            @PathVariable Long meetingId,
            @RequestParam boolean active,
            @CurrentUser User user
    ) {
        meetingService.updateMeetingStatus(meetingId, active, user);
        return ResponseEntity.ok(active ? "모집중으로 변경됨" : "마감 처리됨");
    }

    //     모임 삭제 API
    //     @param id 삭제할 모임 ID
    //     @param user 현재 로그인한 사용자 (호스트 권한 확인용)
    //     @return 204 No Content
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMeeting(@PathVariable Long id, @CurrentUser User user) {
        meetingService.deleteMeeting(id, user);
        return ResponseEntity.noContent().build();
    }
}
