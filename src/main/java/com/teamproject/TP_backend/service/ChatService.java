package com.teamproject.TP_backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.Map;

// Sendbird Open API를 호출하여 채팅 채널 생성 및 초대 기능을 제공하는 서비스 클래스
// - REST API 방식으로 Sendbird 서버와 통신
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final RestTemplate restTemplate = new RestTemplate(); // HTTP 요청 전송 객체
    private final Dotenv dotenv = Dotenv.load(); // .env 파일에서 API 키 등 비밀 정보 로드

    //     Sendbird API 기본 URL 생성
    //     - App ID에 따라 경로가 달라짐
    //     @return https://api-{app_id}.sendbird.com/v3
    private String getBaseUrl() {
        String appId = dotenv.get("SENDBIRD_APP_ID");
        return "https://api-" + appId + ".sendbird.com/v3";
    }

    //     그룹 채널 생성 요청
    //     @param channelName 생성할 채널 이름
    //     @param userIds 채널에 포함시킬 사용자 ID 리스트
    //     @return 생성된 채널 정보를 포함한 JSON 문자열
    public String createGroupChannel(String channelName, List<String> userIds) {
        String url = getBaseUrl() + "/group_channels";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Api-Token", dotenv.get("SENDBIRD_API_TOKEN"));

        Map<String, Object> body = Map.of(
                "name", channelName,
                "user_ids", userIds,
                "is_distinct", false
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        // 바로 여기에 로그 추가!
        log.info("Sendbird 응답: {}", response.getBody());

        try {
            // JSON 파싱해서 channel_url 추출
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            return root.get("channel_url").asText(); // channel_url만 반환

        } catch (Exception e) {
            e.printStackTrace(); // 에러 로그 찍고
            log.error("Sendbird 채널 생성 실패", e); // 예외 로그
            return null;         // 실패 시 null 반환 (또는 throw new RuntimeException...)
        }
    }

    //     기존 그룹 채널에 사용자 초대
    //     @param channelUrl 초대할 채널의 URL (Sendbird 채널 식별자)
    //     @param userIds 초대할 사용자 ID 리스트
    //     @return 초대 결과 JSON 문자열
    public String inviteUser(String channelUrl, List<String> userIds) {
        String url = getBaseUrl() + "/group_channels/" + channelUrl + "/invite";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Api-Token", dotenv.get("SENDBIRD_API_TOKEN"));

        Map<String, Object> body = Map.of("user_ids", userIds);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        return response.getBody();
    }

    // 사용자가 모임에서 퇴장할때 채팅에서도 퇴장하는 메서드
    // @param channelUrl : Sendbird 채널 고유 URL
    // @param userId : 퇴장할 사용자 ID (Sdhendbird user_id와 일치해야 함)
    // @return Sendbird 응답 JSON
    public String leaveChannel(String channelUrl, String userId) {
        String url = getBaseUrl() + "/group_channels/" + channelUrl + "/leave";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Api-Token", dotenv.get("SENDBIRD_API_TOKEN"));

        // 요청 본문에 퇴장할 user_id 명시
        Map<String, Object> body = Map.of("user_ids", List.of(userId));
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        // POST 요청 전송
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        return response.getBody();
    }

    // 호스트가 참여자를 강제퇴장시킬때 채팅에서도 강제퇴장 (혹시 몰라 추가했습니다!)
    public void removeUser(String channelUrl, String userId) {
        String url = getBaseUrl() + "/group_channels/" + channelUrl + "/members/" + userId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Api-Token", dotenv.get("SENDBIRD_API_TOKEN")); // 인증용 토큰

        HttpEntity<Void> request = new HttpEntity<>(headers);
        restTemplate.exchange(url, HttpMethod.DELETE, request, String.class);
    }
}
