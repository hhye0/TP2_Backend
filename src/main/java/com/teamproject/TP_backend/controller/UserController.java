package com.teamproject.TP_backend.controller;

import com.teamproject.TP_backend.config.CurrentUser;
import com.teamproject.TP_backend.config.security.CustomUserDetails;
import com.teamproject.TP_backend.controller.dto.MeetingDTO;
import com.teamproject.TP_backend.controller.dto.UserDTO;
import com.teamproject.TP_backend.controller.dto.UserUpdateRequestDTO;
import com.teamproject.TP_backend.domain.entity.User;
import com.teamproject.TP_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 사용자 정보 관리 관련 API를 제공하는 컨트롤러
// - 회원 정보 수정
// - 회원 탈퇴
// - 마이페이지 조회(/me)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    // 마이페이지 사용자 정보 조회
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getMyInfo(@CurrentUser User user) {
        return ResponseEntity.ok(UserDTO.from(user));
    }

    // 사용자가 참여한 모임 목록 조회
    @GetMapping("/me/meetings/joined")
    public ResponseEntity<List<MeetingDTO>> getJoinedMeetings(@CurrentUser User user) {
        if (user == null || user.getId() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User userEntity = userService.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
        return ResponseEntity.ok(userService.getJoinedMeetings(userEntity));
    }
    // 호스트인 모임 목록 조회
    @GetMapping("/me/hosted")
    public ResponseEntity<List<MeetingDTO>> getHostedMeetings(@CurrentUser User user) {
        List<MeetingDTO> meetings = userService.getHostedMeetings(user);
        return ResponseEntity.ok(meetings);
    }

    //     현재 로그인한 사용자의 회원 정보 수정 API
    //     - 이름, 이메일, 비밀번호 변경 가능
    //     @param dto 수정할 회원 정보
    //     @param authentication 인증 정보 (Spring Security로부터 주입)
    //     @return 성공 메시지 or 실패 응답
    @PatchMapping("/me")
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateRequestDTO dto, Authentication authentication) {
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getUser().getId(); // 인증된 사용자 ID
        boolean updated = userService.updateUser(userId, dto);
        if (updated) {
            return ResponseEntity.ok("회원정보 수정 완료");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("회원 정보를 찾을 수 없습니다.");
        }
    }

    //     회원 탈퇴 API
    //     - 사용자 ID를 전달받아 삭제 요청 처리
    //     @param id 탈퇴할 사용자 ID
    //     @return 성공/실패 메시지
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            return ResponseEntity.ok("계정 탈퇴 완료");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("회원 정보를 찾을 수 없습니다.");
        }
    }
}
