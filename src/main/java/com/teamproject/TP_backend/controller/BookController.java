package com.teamproject.TP_backend.controller;

import com.teamproject.TP_backend.config.CurrentUser;
import com.teamproject.TP_backend.controller.dto.BookDTO;
import com.teamproject.TP_backend.controller.dto.BookReviewRequestDTO;
import com.teamproject.TP_backend.controller.dto.BookReviewResponseDTO;
import com.teamproject.TP_backend.controller.dto.BookReviewUpdateDTO;
import com.teamproject.TP_backend.domain.entity.User;
import com.teamproject.TP_backend.service.BookReviewService;
import com.teamproject.TP_backend.service.BookSearchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {

    private final BookReviewService bookReviewService;
    private final BookSearchService bookSearchService;

    // 전체 조회
    @GetMapping("/review")
    public ResponseEntity<List<BookReviewResponseDTO>> getAllReviews(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        List<BookReviewResponseDTO> reviews = bookReviewService.getAllReviews(pageable).getContent();
        return ResponseEntity.ok(reviews);
    }

    // 리뷰 생성(알라딘 API에서 책 제목 검색 후 첫 결과로 리뷰 저장)
    @PostMapping("/review")
    public ResponseEntity<?> addReview(@RequestBody BookReviewRequestDTO dto,
                                       @CurrentUser User user) {
        try {
            List<BookDTO> results = bookSearchService.searchBookByTitle(dto.getBookTitle());

            if (results.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "책을 찾을 수 없습니다."));
            }

            BookDTO book = results.get(0);
            bookReviewService.addReview(book.getTitle(), dto.getMessage(), user);

            return ResponseEntity.ok("리뷰가 저장되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", "리뷰 저장 중 오류가 발생했습니다."));
        }
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
        // 책 제목으로 알라딘 API 검색 (프론트에서 책 검색용)
        @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<?> searchBook(@RequestParam String title) {
            try {
                String decodedTitle = URLDecoder.decode(title, StandardCharsets.UTF_8);
                List<BookDTO> results = bookSearchService.searchBookByTitle(decodedTitle);

                if (results.isEmpty()) {
                    return ResponseEntity.badRequest().body(Map.of("error", "책을 찾을 수 없습니다."));
                }

                return ResponseEntity.ok(results);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.badRequest().body(Map.of("error", "검색 중 오류가 발생했습니다."));
            }
        }

}
