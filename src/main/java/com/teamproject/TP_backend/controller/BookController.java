package com.teamproject.TP_backend.controller;

import com.teamproject.TP_backend.controller.dto.BookTitleAuthorCoverDto;
import com.teamproject.TP_backend.service.BookSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {

    private final BookSearchService bookSearchService;

    @GetMapping("/search")
    public List<BookTitleAuthorCoverDto> searchBooks(@RequestParam String title) throws Exception {
        return bookSearchService.searchBooksByTitle(title);
    }
}
