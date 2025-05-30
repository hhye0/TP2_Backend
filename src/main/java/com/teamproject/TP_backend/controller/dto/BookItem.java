package com.teamproject.TP_backend.controller.dto;

import lombok.Data;

@Data
public class BookItem {
    private String title;
    private String link;
    private String author;
    private String pubDate;
    private String description;
    private String creator;
    private String isbn;
    private String isbn13;
    private long itemId;
    private int priceSales;
    private int priceStandard;
    private String stockStatus;
    private int mileage;
    private String cover;
    private int categoryId;
    private String categoryName;
    private String publisher;
    private int customerReviewRank;
}
