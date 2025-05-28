package com.teamproject.TP_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;  // 회원 고유 식별자 (시스템 자동 생성)

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;  // 사용자 이메일

    @Column(name = "password", nullable = false, length = 255)
    private String password;  // 암호화된 비밀번호 (BCrypt 암호화)

    @Column(name = "name", nullable = false, length = 255)
    private String name;  // 사용자 이름
}
