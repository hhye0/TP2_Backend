package com.teamproject.TP_backend.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.teamproject.TP_backend.domain.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

// 사용자(User) 도메인 엔티티입니다.
// 회원 가입 정보, 역할, 참여 모임 정보 등을 관리합니다.
@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id; // 사용자 고유 ID (PK)

    @Column(nullable = false)
    @NotBlank(message = "Name은 필수 입력항목입니다.")
    private String name; // 사용자 이름

    @Column(nullable = false, unique = true, length = 100)
    @NotBlank(message = "Email은 필수 입력항목입니다.")
    @Email
    private String email; // 사용자 이메일 (로그인 ID 역할)

    @Column(nullable = false, length = 255)
    private String password; // 해싱된 사용자 비밀번호

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.USER; // 사용자 역할 (기본 USER)

    //     사용자가 호스트로 개설한 모임 목록
//     - OneToMany(주인 X) / mappedBy="host"
    @OneToMany(mappedBy = "host", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Meeting> hostedMeetings = new ArrayList<>();

    //     사용자가 참여한 모임 목록 (참여자 입장)
//     - OneToMany(주인 X) / mappedBy="user"
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MeetingMember> meetingMemberships = new ArrayList<>();

    // 사용자가 작성한 책 리뷰 목록
    //     - OneToMany(주인 X) / mappedBy="user"
     @OneToMany(mappedBy = "reviewer", cascade = CascadeType.ALL, orphanRemoval = true)
     private List<BookReview> bookReviews = new ArrayList<>();
}
