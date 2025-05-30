package com.teamproject.TP_backend.domain.entity;

import com.teamproject.TP_backend.controller.dto.BookDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Book {
    @Id
    private String isbn;

    private String title;
    private String author;
    private String cover;

    // 생성자, getter/setter
    public Book(BookDTO dto) {
        this.isbn = dto.getIsbn();
        this.title = dto.getTitle();
        this.author = dto.getAuthor();
        this.cover = dto.getCover();
    }
}