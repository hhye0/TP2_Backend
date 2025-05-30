package com.teamproject.TP_backend.service;

import com.teamproject.TP_backend.controller.dto.RecommendRequestDto;
import com.teamproject.TP_backend.domain.entity.Book;
import com.teamproject.TP_backend.domain.entity.Recommendation;
import com.teamproject.TP_backend.domain.entity.User;
import com.teamproject.TP_backend.repository.RecommendationRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;
    private final BookService bookService;
    private final UserService userService;


    public void createRecommendation(User fromUser, RecommendRequestDto dto) {
        Book book = bookService.findOrSave(dto.getBook());

        // 추천받는 대상자 조회
        User toUser = userService.findById(dto.getToUserId())
                .orElseThrow(() -> new RuntimeException("추천 대상 유저를 찾을 수 없습니다."));

        Recommendation recommendation = new Recommendation(
                fromUser,  // JWT에서 받아온 작성자
                toUser,
                book,
                dto.getMessage()
        );

        recommendationRepository.save(recommendation);
    }

}
