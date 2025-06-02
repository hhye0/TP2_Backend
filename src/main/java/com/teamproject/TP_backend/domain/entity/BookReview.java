package com.teamproject.TP_backend.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 책 리뷰 정보를 저장하는 엔티티 클래스
// - 사용자가 작성한 도서 리뷰를 DB에 저장합니다.
// - 책 제목, 저자, 표지 URL, 리뷰 내용, 작성 시간, 작성자(User) 포함
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookReview {

    // 리뷰 고유 ID (기본 키)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 리뷰의 고유 식별자 (PK)

    private String bookTitle; // 책 제목

    private String bookAuthor; // 책 저자

    private String bookCoverUrl; // 표지 URL

    private String message; // 리뷰 메시지

    private LocalDateTime createdAt; // 리뷰가 작성된 시점

    // 리뷰 작성자 (User 엔티티와 다대일 관계)
    // - 하나의 유저가 여러 개의 리뷰를 작성할 수 있음
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer")
    private User reviewer;

    public void updateMessage(String newMessage) {
        this.message = newMessage;
    }
}