package com.teamproject.TP_backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamproject.TP_backend.controller.dto.AladinSearchResultDTO;
import com.teamproject.TP_backend.controller.dto.BookDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookSearchService {

    @Value("${aladin.api.key}")
    private String apiKey;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String ALADIN_API_URL = "https://www.aladin.co.kr/ttb/api/ItemSearch.aspx";

    public List<BookDTO> searchBooksByTitle(String title) throws Exception {
        URI uri = UriComponentsBuilder.fromHttpUrl(ALADIN_API_URL)
                .queryParam("ttbkey", apiKey)
                .queryParam("Query", title)
                .queryParam("QueryType", "Title")
                .queryParam("MaxResults", 10)
                .queryParam("start", 1)
                .queryParam("SearchTarget", "Book")
                .queryParam("output", "js")  // JSON 형식 요청
                .build()
                .encode()
                .toUri();

        String jsonResponse = restTemplate.getForObject(uri, String.class);

        AladinSearchResultDTO result = objectMapper.readValue(jsonResponse, AladinSearchResultDTO.class);

        // BookItem 리스트에서 필요한 데이터만 추출
        return result.getItems().stream()
                .map(item -> new BookDTO(
                        item.getIsbn(),
                        item.getTitle(),
                        item.getAuthor(),
                        item.getCover()
                ))
                .collect(Collectors.toList());
    }
}