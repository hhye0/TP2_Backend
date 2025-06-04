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

    // 작성자 정보
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    // 정적 팩토리 메서드
    public static Notice create(String title, String content, User author) {
        Notice notice = new Notice();
        notice.title = title;
        notice.content = content;
        notice.author = author;
        return notice;
    }
}
