package com.teamproject.TP_backend.controller;

import com.teamproject.TP_backend.config.CurrentUser;
import com.teamproject.TP_backend.controller.dto.BookReviewRequestDto;
import com.teamproject.TP_backend.domain.entity.User;
import com.teamproject.TP_backend.service.BookReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {

    private final BookReviewService bookReviewService;

    @PostMapping("/review")
    public ResponseEntity<?> addReview(@RequestBody BookReviewRequestDto dto,
                                       @CurrentUser User user) {
        bookReviewService.addReview(
                dto.getBookTitle(),
                dto.getMessage(),
                user.getId()
        );
        return ResponseEntity.ok("리뷰가 저장되었습니다.");
    }
}
