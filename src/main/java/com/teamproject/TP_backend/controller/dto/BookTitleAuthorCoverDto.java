package com.teamproject.TP_backend.controller.dto;

import lombok.Data;

@Data
public class BookTitleAuthorCoverDto {
    private String title;
    private String author;
    private String cover;

    // 생성자
    public BookTitleAuthorCoverDto(String title, String author, String cover) {
        this.title = title;
        this.author = author;
        this.cover = cover;
    }
}