package com.teamproject.TP_backend.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {

    @JsonProperty("isbn13")  // JSON 필드명이 isbn13일 경우 매핑
    private String isbn;

    private String title;
    private String author;
    private String cover;
}