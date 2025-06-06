package com.teamproject.TP_backend.controller;

import com.teamproject.TP_backend.config.CurrentUser;
import com.teamproject.TP_backend.controller.dto.NoticeRequestDTO;
import com.teamproject.TP_backend.controller.dto.NoticeResponseDTO;
import com.teamproject.TP_backend.domain.entity.User;
import com.teamproject.TP_backend.service.NoticeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/meetings/{meetingId}/notices")
public class NoticeController {

    private final NoticeService noticeService;

    // 공지사항 등록 (POST)
    @PostMapping
    public ResponseEntity<NoticeResponseDTO> createNotice(
            @PathVariable Long meetingId,
            @RequestBody @Valid NoticeRequestDTO dto,
            @CurrentUser User user) {
        NoticeResponseDTO response = noticeService.createNotice(dto, meetingId, user.getNickname());
        return ResponseEntity.ok(response);
    }

    // 모임별 공지사항 전체 조회 (GET)
    @GetMapping
    public ResponseEntity<List<NoticeResponseDTO>> getNoticesByMeetingId(@PathVariable Long meetingId) {
        List<NoticeResponseDTO> list = noticeService.getNoticesByMeetingId(meetingId);
        return ResponseEntity.ok(list);
    }

    // 공지사항 단건 조회 (GET /{id})
    @GetMapping("/{id}")
    public ResponseEntity<NoticeResponseDTO> getNoticeById(@PathVariable Long id) {
        NoticeResponseDTO response = noticeService.getNoticeById(id);
        return ResponseEntity.ok(response);
    }

    // 공지사항 수정
    @PutMapping("/{id}")
    public ResponseEntity<?> updateNotice(
            @PathVariable Long id,
            @RequestBody @Valid NoticeRequestDTO dto,
            @CurrentUser User user
    ) {
        NoticeResponseDTO updated = noticeService.updateNotice(id, dto, user.getNickname());
        return ResponseEntity.ok(updated);
    }

    // 공지사항 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotice(
            @PathVariable Long id,
            @CurrentUser User user
    ) {
        noticeService.deleteNotice(id, user.getNickname());
        return ResponseEntity.ok(Map.of("message", "공지사항이 삭제되었습니다."));
    }
}