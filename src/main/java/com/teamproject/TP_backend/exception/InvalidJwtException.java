package com.teamproject.TP_backend.exception;

// 유효하지 않은 JWT 토큰 사용 시 발생하는 커스텀 예외 클래스
// - 토큰 파싱 오류, 서명 불일치, 만료 등 다양한 JWT 예외 상황을 포괄
public class InvalidJwtException extends RuntimeException {

    //     기본 생성자
    public InvalidJwtException() {
        super();
    }

    //     예외 메시지를 포함하는 생성자
    //     @param message 상세 예외 메시지
    public InvalidJwtException(String message) {
        super(message);
    }

    //     메시지와 원인 예외를 포함하는 생성자
    //     @param message 상세 예외 메시지
    //     @param cause 원인이 되는 예외 객체
    public InvalidJwtException(String message, Throwable cause) {
        super(message, cause);
    }
}
