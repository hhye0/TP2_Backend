package com.teamproject.TP_backend.exception;

// 이미 등록된 이메일로 회원가입을 시도할 때 발생하는 예외
// - 이메일 중복 검사 로직에서 사용
public class UserAlreadyExistsException extends RuntimeException {

//     예외 메시지를 포함하는 생성자
//     @param message 중복 가입 시 사용자에게 전달할 메시지
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
