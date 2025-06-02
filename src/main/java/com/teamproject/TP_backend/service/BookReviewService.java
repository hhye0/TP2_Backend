package com.teamproject.TP_backend.service;

import com.teamproject.TP_backend.controller.dto.BookDTO;
import com.teamproject.TP_backend.controller.dto.BookReviewResponseDTO;
import com.teamproject.TP_backend.domain.entity.BookReview;

import com.teamproject.TP_backend.domain.entity.User;
import com.teamproject.TP_backend.exception.ReviewNotFoundException;
import com.teamproject.TP_backend.exception.UnauthorizedAccessException;
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
    public BookReviewResponseDTO addReview(String bookTitle, String message, User reviewer) {
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

        BookReview savedReview = bookReviewRepository.save(bookReview);
        return convertToDTO(savedReview);
    }

    // 리뷰 수정
    @Transactional
    public BookReviewResponseDTO updateReview(Long reviewId, String newMessage, User user) {
        BookReview review = bookReviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("리뷰를 찾을 수 없습니다."));

        if (!review.getReviewer().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("자신의 리뷰만 수정할 수 있습니다.");
        }

        review.updateMessage(newMessage);  // 엔티티에 update 메서드 추가
        return convertToDTO(review);
    }

    // 리뷰 삭제
    @Transactional
    public void deleteReview(Long reviewId, User user) {
        BookReview review = bookReviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("리뷰를 찾을 수 없습니다."));

        if (!review.getReviewer().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("자신의 리뷰만 삭제할 수 있습니다.");
        }

        bookReviewRepository.delete(review);
    }

    // Entity -> DTO 변환
    private BookReviewResponseDTO convertToDTO(BookReview review) {
        return BookReviewResponseDTO.builder()
                .reviewId(review.getId())
                .bookTitle(review.getBookTitle())
                .bookAuthor(review.getBookAuthor())
                .bookCoverUrl(review.getBookCoverUrl())
                .message(review.getMessage())
                .reviewerNickname(review.getReviewer().getNickname())  // User 엔티티에 nickname 필드 필요
                .createdAt(review.getCreatedAt())
                .build();
    }
}
