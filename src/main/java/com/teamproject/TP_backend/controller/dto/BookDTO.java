package com.teamproject.TP_backend.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BookDTO {
    @JsonProperty("isbn13")
    private String isbn;
    private String title;
    private String author;
    private String cover;

    // 생성자
    public BookDTO(String isbn, String title, String author, String cover) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.cover = cover;
    }
}