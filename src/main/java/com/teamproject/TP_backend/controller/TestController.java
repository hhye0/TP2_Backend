package com.teamproject.TP_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 인증 테스트용 간단한 컨트롤러
// JWT 인증이 정상적으로 작동하는지 확인할 때 사용
@RestController
@RequestMapping("/api")
public class TestController {

//     인증 테스트 엔드포인트
//     - 토큰이 필요한 API로, 인증 성공 시 문자열을 반환
//     - Spring Security 설정에서 해당 엔드포인트에 인증 필터가 적용되어 있어야 함.
//     @return "인증 성공!" 문자열
    @GetMapping("/test")
    public ResponseEntity<String> testAuth() {
        return ResponseEntity.ok("인증 성공!");
    }
}
