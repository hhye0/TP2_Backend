package com.teamproject.TP_backend.service;

import com.teamproject.TP_backend.controller.dto.BookDTO;
import com.teamproject.TP_backend.domain.entity.BookReview;

import com.teamproject.TP_backend.domain.entity.User;
import com.teamproject.TP_backend.repository.BookReviewRepository;
import com.teamproject.TP_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookReviewService {

    private final BookSearchService bookSearchService;
    private final BookReviewRepository bookReviewRepository;
    private final UserRepository userRepository;


    @Transactional
    public void addReview(String bookTitle, String message, Long reviewerUserId) {
        // 유저 정보 조회
        User reviewer = userRepository.findById(reviewerUserId)
                .orElseThrow(() -> new RuntimeException("작성자 정보를 찾을 수 없습니다."));

        // 책 검색
        List<BookDTO> results = bookSearchService.searchBookByTitle(bookTitle);
        if (results.isEmpty()) {
            throw new RuntimeException("책 정보를 찾을 수 없습니다.");
        }

        BookDTO book = results.get(0);

        BookReview bookReview = BookReview.builder()
                .bookTitle(book.getTitle())
                .bookAuthor(book.getAuthor())
                .bookCoverUrl(book.getCover())
                .message(message)
                .reviewer(reviewer)
                .createdAt(LocalDateTime.now())
                .build();

        bookReviewRepository.save(bookReview);
    }

}

