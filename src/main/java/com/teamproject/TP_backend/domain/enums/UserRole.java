package com.teamproject.TP_backend.domain.enums;

// 사용자 권한(Role)을 정의하는 열거형(Enum)
// - 시스템 내에서 사용자 역할에 따라 접근 권한을 구분할 때 사용
public enum UserRole {

    USER,   // 일반 사용자 (기본값)
    ADMIN   // 관리자 (운영자 권한 보유)
}
