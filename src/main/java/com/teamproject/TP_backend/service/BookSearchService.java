package com.teamproject.TP_backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamproject.TP_backend.controller.dto.BookDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

// 외부 알라딘 API를 호출해서 책 정보를 검색
@Slf4j
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

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
            log.info("알라딘 요청 URI: {}", uri);
            log.info("알라딘 응답 본문: {}", response.getBody());

            // 응답 JSON 문자열을 파싱하여 루트 노드 생성
            JsonNode rootNode = new ObjectMapper().readTree(response.getBody());
            JsonNode items = rootNode.get("item"); // 'item' 배열 노드 추출 (책 리스트)

            if (items == null || !items.isArray() || items.size() == 0) {
                log.warn(" 검색 결과 없음 또는 'item' 배열 누락");
                return List.of(); // 빈 리스트 반환 (예외 안 던짐)
            }

            JsonNode first = items.get(0);

            // 안전한 필드 추출 및 기본값 지정
            String isbn = first.hasNonNull("isbn13") ? first.get("isbn13").asText() :
                    first.hasNonNull("isbn") ? first.get("isbn").asText() : "NO_ISBN";

            String titleResult = first.hasNonNull("title") ? first.get("title").asText() : "제목 없음";
            String author = first.hasNonNull("author") ? first.get("author").asText() : "저자 미상";
            String cover = first.hasNonNull("coverLargeUrl") ? first.get("coverLargeUrl").asText() :
                    first.hasNonNull("cover") ? first.get("cover").asText() : "";

            return List.of(new BookDTO(isbn, titleResult, author, cover));

        } catch (Exception e) {
            log.error("알라딘 API 처리 중 예외 발생", e);
            throw new RuntimeException("책 검색 실패: " + e.getMessage());
        }
    }
}