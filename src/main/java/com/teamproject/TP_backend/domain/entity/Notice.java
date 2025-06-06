package com.teamproject.TP_backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    // 작성자 (User 엔티티와 연관관계)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", nullable = false)
    private User writer;

    // 소속 모임
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    // 정적 팩토리 메서드
    public static Notice create(String title, String content, User writer, Meeting meeting) {
        Notice notice = new Notice();
        notice.title = title;
        notice.content = content;
        notice.meeting = meeting;
        notice.writer = writer;
        return notice;
    }
}
