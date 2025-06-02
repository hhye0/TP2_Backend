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

// 외부 알라딘 API를 호출해서 책 정보를 검색
@Service
@RequiredArgsConstructor
public class BookSearchService {

    @Value("${aladin.api.key}")
    private String apiKey;
    private final RestTemplate restTemplate = new RestTemplate();

    private final String ALADIN_API_URL = "https://www.aladin.co.kr/ttb/api/ItemSearch.aspx";

    /**
     * 책 제목을 받아 알라딘 API를 통해 책 정보를 검색하고,
     * 첫 번째 검색 결과를 BookDTO로 변환하여 반환한다.
     *
     * @param title 검색할 책 제목
     * @return 검색된 책 정보를 담은 BookDTO 리스트 (최대 1개)
     */
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

        // API GET 요청 수행 후 응답 수신
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        System.out.println("Aladin API Response body: " + response.getBody());

        try {
            // 응답 JSON 문자열을 파싱하여 루트 노드 생성
            JsonNode rootNode = new ObjectMapper().readTree(response.getBody());
            JsonNode items = rootNode.get("item"); // 'item' 배열 노드 추출 (책 리스트)

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