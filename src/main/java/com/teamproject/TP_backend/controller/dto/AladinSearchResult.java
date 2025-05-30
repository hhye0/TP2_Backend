package com.teamproject.TP_backend.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class AladinSearchResult {
    private String version;
    private String title;
    private String link;
    private String pubDate;
    private String imageUrl;
    private int totalResults;
    private int startIndex;
    private int itemsPerPage;
    private String query;
    private int searchCategoryId;
    private String searchCategoryName;

    @JsonProperty("item")
    private List<BookItem> items;

}