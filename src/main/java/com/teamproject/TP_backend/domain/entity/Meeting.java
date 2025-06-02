package com.teamproject.TP_backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// 모임(Meeting) 도메인 엔티티입니다.
// 책 모임의 기본 정보, 호스트, 참여자 목록 등을 관리합니다.
@Entity
@Table(name = "meetings")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 모임 ID (PK)

    @Column(nullable = false, length = 200)
    private String title; // 모임명

    @Column(name = "description", columnDefinition = "TEXT")
    private String description; // 모임 소개글

    @Column(name = "book_title", nullable = false, length = 100)
    private String bookTitle; // 읽을 책 제목

    @Column(name = "book_author", nullable = false, length = 100)
    private String bookAuthor; // 책 저자

    @Column(name = "book_cover", length = 255)
    private String bookCover; // 책 표지 이미지 URL

    @Column(name = "book_category", length = 100)
    private String bookCategory; // 책 장르/카테고리

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate; // 모임 시작 날짜 및 시간

    @Column(name = "max_members", nullable = false)
    private int maxMembers; // 최대 참여 인원

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true; // 모임 활성 상태 (삭제 여부)

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt; // 생성일 (자동 설정)

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // 수정일 (자동 설정)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id", nullable = false)
    private User host; // 모임 개설자 (호스트)

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MeetingMember> participants = new ArrayList<>(); // 참여자 목록

    //     전체 필드를 받는 @Builder 생성자
    //     - DTO → Entity 변환 시 주로 사용
    @Builder
    public Meeting(Long id, String title, String description, String bookTitle, String bookAuthor, String bookCover,
                   String bookCategory, LocalDateTime startDate, int maxMembers, boolean isActive,
                   LocalDateTime createdAt, LocalDateTime updatedAt, User host, List<MeetingMember> participants) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.bookCover = bookCover;
        this.bookCategory = bookCategory;
        this.startDate = startDate;
        this.maxMembers = maxMembers;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.host = host;
        this.participants = participants != null ? participants : new ArrayList<>();
    }

    // 여기 부분 확인 ( 안 쓰이면 삭제 )
    //     참여자 추가 편의 메서드
    public void addParticipant(MeetingMember participant) {
        participants.add(participant);
        participant.setMeeting(this);
    }

    //     참여자 제거 편의 메서드
    public void removeParticipant(MeetingMember participant) {
        participants.remove(participant);
        participant.setMeeting(null);
    }

    //     모임 수정용 편의 메서드
    public void update(String title, String bookTitle, String bookAuthor, String bookCover,
                       String bookCategory, LocalDateTime startDate,
                       int maxMembers, User host) {
        this.title = title;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.bookCover = bookCover;
        this.bookCategory = bookCategory;
        this.startDate = startDate;
        this.maxMembers = maxMembers;
        this.host = host;
    }
}
