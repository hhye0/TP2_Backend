package com.teamproject.TP_backend.service;

import com.teamproject.TP_backend.controller.dto.BookDTO;
import com.teamproject.TP_backend.domain.entity.Book;
import com.teamproject.TP_backend.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    // isbn으로 책을 찾고, 없으면 저장 후 반환
    public Book findOrSave(BookDTO dto) {
        if (dto.getIsbn() == null || dto.getIsbn().isBlank()) {
            throw new IllegalArgumentException("ISBN 값이 없어 책 정보를 저장할 수 없습니다.");
        }
        return bookRepository.findById(dto.getIsbn())
                .orElseGet(() -> bookRepository.save(new Book(dto)));
    }

}
