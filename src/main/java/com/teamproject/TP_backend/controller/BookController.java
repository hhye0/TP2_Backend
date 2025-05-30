package com.teamproject.TP_backend.controller;

import com.teamproject.TP_backend.config.CurrentUser;
import com.teamproject.TP_backend.controller.dto.BookDTO;
import com.teamproject.TP_backend.controller.dto.RecommendRequestDto;
import com.teamproject.TP_backend.domain.entity.Book;
import com.teamproject.TP_backend.domain.entity.User;
import com.teamproject.TP_backend.service.BookSearchService;
import com.teamproject.TP_backend.service.BookService;
import com.teamproject.TP_backend.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {

    private final BookSearchService bookSearchService;
    private final RecommendationService recommendationService;
    private final BookService bookService;

    @GetMapping("/search")
    public List<BookDTO> searchBooks(@RequestParam String title) throws Exception {
        return bookSearchService.searchBooksByTitle(title);
    }

    @PostMapping("/recommend")
    public ResponseEntity<?> recommendBook(@CurrentUser User currentUser,
                                           @RequestBody RecommendRequestDto dto) {
        recommendationService.createRecommendation(currentUser, dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/select")
    public ResponseEntity<Book> selectBook(@RequestBody BookDTO dto) {
        Book book = bookService.findOrSave(dto);
        return ResponseEntity.ok(book);
    }


}
