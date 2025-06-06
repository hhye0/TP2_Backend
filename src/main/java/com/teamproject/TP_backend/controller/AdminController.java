package com.teamproject.TP_backend.controller;

import com.teamproject.TP_backend.config.CurrentUser;
import com.teamproject.TP_backend.controller.dto.MeetingDTO;
import com.teamproject.TP_backend.domain.entity.User;
import com.teamproject.TP_backend.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // 모든 모임 조회 (관리자 권한 필수)
    @GetMapping("/meetings")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MeetingDTO>> getAllMeetings(@CurrentUser User user) {
        List<MeetingDTO> meetings = adminService.getAllMeetings(user);
        return ResponseEntity.ok(meetings);
    }
}

