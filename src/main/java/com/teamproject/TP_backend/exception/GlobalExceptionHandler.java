package com.teamproject.TP_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

// 전역(Global) 예외 처리를 담당하는 클래스
// 컨트롤러에서 발생하는 예외들을 공통적으로 처리하여 일관된 응답을 제공
@RestControllerAdvice
public class GlobalExceptionHandler {

//     인증 실패 예외 처리
//     - Spring Security 인증 과정에서 아이디 또는 비밀번호가 틀릴 경우 발생
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentials(BadCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "아이디 또는 비밀번호가 틀렸습니다."));
    }

//     회원가입 시 이미 존재하는 이메일로 가입 시도 시 발생
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> handleUserAlreadyExists(UserAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", e.getMessage()));
    }

//     유효하지 않은 JWT 토큰 사용 시 발생
//     - 서명 오류, 만료, 파싱 실패 등
    @ExceptionHandler(InvalidJwtException.class)
    public ResponseEntity<?> handleInvalidJwt(InvalidJwtException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "유효하지 않은 JWT 토큰입니다."));
    }

//     처리되지 않은 예외(기타 모든 예외) 처리
//     - 예기치 않은 서버 오류에 대한 기본 응답
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "서버 내부 오류가 발생했습니다."));
    }
}
