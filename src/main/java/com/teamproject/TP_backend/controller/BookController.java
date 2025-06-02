package com.teamproject.TP_backend.controller;

import com.teamproject.TP_backend.config.CurrentUser;
import com.teamproject.TP_backend.controller.dto.BookReviewRequestDTO;
import com.teamproject.TP_backend.controller.dto.BookReviewResponseDTO;
import com.teamproject.TP_backend.controller.dto.BookReviewUpdateDTO;
import com.teamproject.TP_backend.domain.entity.User;
import com.teamproject.TP_backend.service.BookReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {

    private final BookReviewService bookReviewService;

    // 전체 조회
    @GetMapping("/review")
    public ResponseEntity<List<BookReviewResponseDTO>> getAllReviews(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        List<BookReviewResponseDTO> reviews = bookReviewService.getAllReviews(pageable).getContent();
        return ResponseEntity.ok(reviews);
    }

    // 리뷰 생성
    @PostMapping("/review")
    public ResponseEntity<?> addReview(@RequestBody BookReviewRequestDTO dto,
                                       @CurrentUser User user) {
        bookReviewService.addReview(
                dto.getBookTitle(),
                dto.getMessage(),
                user
        );
        return ResponseEntity.ok("리뷰가 저장되었습니다.");
    }

    // 리뷰 수정
    @PutMapping("/review/{reviewId}")
    public ResponseEntity<?> updateReview(
            @PathVariable Long reviewId,
            @Valid @RequestBody BookReviewUpdateDTO dto,
            @CurrentUser User user) {
        BookReviewResponseDTO response = bookReviewService.updateReview(
                reviewId,
                dto.getMessage(),
                user
        );
        return ResponseEntity.ok(response);
    }

    // 리뷰 삭제
    @DeleteMapping("/review/{reviewId}")
    public ResponseEntity<?> deleteReview(
            @PathVariable Long reviewId,
            @CurrentUser User user) {
        bookReviewService.deleteReview(reviewId, user);
        return ResponseEntity.ok("리뷰가 삭제되었습니다.");
    }

}
