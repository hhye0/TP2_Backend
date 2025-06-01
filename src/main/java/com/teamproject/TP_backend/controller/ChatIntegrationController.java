package com.teamproject.TP_backend.controller;

import com.teamproject.TP_backend.service.SendbirdService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Sendbird 채팅 연동을 위한 API 컨트롤러
// 그룹 채널 생성 및 유저 초대 기능을 제공
// 경로: /api/sendbird
@RestController
@RequestMapping("/api/sendbird")
@RequiredArgsConstructor
public class ChatIntegrationController {

    private final SendbirdService sendbirdService; // Sendbird API 연동 서비스

//     그룹 채널 생성 요청
//     - 채널 이름과 참여할 사용자 ID 리스트를 전달받아 Sendbird 채널 생성
//     @param channelName 생성할 채널 이름
//     @param userIds 채널에 참여시킬 사용자 ID 리스트
//     @return 생성된 채널 URL 또는 채널 ID
@PostMapping("/channels")
    public String createChannel(
            @RequestParam String channelName,
            @RequestBody List<String> userIds
    ) {
        return sendbirdService.createGroupChannel(channelName, userIds);
    }

//      기존 채널에 사용자 초대 요청
//     - 채널 URL과 초대할 사용자 ID 리스트를 전달받아 초대 처리
//     @param channelUrl 초대할 대상 채널의 URL
//     @param userIds 초대할 사용자 ID 리스트
//     @return 초대 결과 메시지
@PostMapping("/channels/{channelUrl}/invite")
    public String inviteUser(
            @PathVariable String channelUrl,
            @RequestBody List<String> userIds
    ) {
        return sendbirdService.inviteUser(channelUrl, userIds);
    }
}
