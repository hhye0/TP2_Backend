package com.teamproject.TP_backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamproject.TP_backend.controller.dto.BookDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookSearchService {

    @Value("${aladin.api.key}")
    private String apiKey;
    private final RestTemplate restTemplate = new RestTemplate();

    private final String ALADIN_API_URL = "https://www.aladin.co.kr/ttb/api/ItemSearch.aspx";

    public List<BookDTO> searchBookByTitle(String title) {

        URI uri = UriComponentsBuilder.fromHttpUrl(ALADIN_API_URL)
                .queryParam("ttbkey", apiKey)
                .queryParam("Query", title) // "해리포터" 같은 한글도 OK
                .queryParam("QueryType", "Title")
                .queryParam("MaxResults", 5)
                .queryParam("start", 1)
                .queryParam("Version", "20131101")
                .queryParam("SearchTarget", "Book")
                .queryParam("output", "js")
                .encode(StandardCharsets.UTF_8)  // 한국어 사용 가능
                .build()
                .toUri();

        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        System.out.println("Aladin API Response body: " + response.getBody());

        try {
            JsonNode rootNode = new ObjectMapper().readTree(response.getBody());
            JsonNode items = rootNode.get("item");

            if (items == null || !items.isArray() || items.size() == 0) {
                throw new RuntimeException("검색 결과 없음. 응답: " + response.getBody());
            }

            JsonNode first = items.get(0);
            return List.of(new BookDTO(
                    first.has("isbn13") ? first.get("isbn13").asText() : first.get("isbn").asText(),
                    first.get("title").asText(),
                    first.get("author").asText(),
                    first.has("coverLargeUrl") ? first.get("coverLargeUrl").asText() : first.get("cover").asText()
            ));
        } catch (Exception e) {
            throw new RuntimeException("API 응답: " + response.getBody(), e);
        }
    }
}