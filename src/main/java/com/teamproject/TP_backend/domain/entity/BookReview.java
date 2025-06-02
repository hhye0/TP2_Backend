package com.teamproject.TP_backend.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 리뷰의 고유 식별자 (PK)

    private String bookTitle; // 책 제목

    private String bookAuthor; // 책 저자

    private String bookCoverUrl; // 표지 URL

    private String message; // 리뷰 메시지

    private LocalDateTime createdAt; // 리뷰가 작성된 시점

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer")
    private User reviewer;

    public void updateMessage(String newMessage) {
        this.message = newMessage;
    }
}