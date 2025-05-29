package com.teamproject.TP_backend.runner;

import com.teamproject.TP_backend.entity.Meeting;
import com.teamproject.TP_backend.entity.User;
import com.teamproject.TP_backend.repository.MeetingRepository;
import com.teamproject.TP_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Order(1)
public class TestDataRunner implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MeetingRepository meetingRepository;
    private User host;

    @Override
    public void run(String... args) {
        if (!userRepository.existsByEmail("test@email.com")) {
            User user = User.builder()
                    .name("홍길동")
                    .email("test@email.com")
                    .password(passwordEncoder.encode("1234abcd!"))
                    .build();
            host = userRepository.save(user);
            System.out.println(">> 테스트 유저 생성 완료");
        }

        // 미팅/ 게시물
        if (meetingRepository.count() == 0) {
            Meeting meeting1 = Meeting.builder()
                    .title("함께하는 심리학 독서모임")
                    .bookTitle("자기 이해의 심리학")
                    .bookAuthor("이민규")
                    .bookCover("https://example.com/psychology.jpg")
                    .bookCategory("심리학")
                    .startDate(LocalDateTime.of(2025, 6, 10, 10, 0))
                    .maxMembers(15)
                    .isActive(true)
                    .host(host)  // ✅ host 필드 설정

                    .build();

            Meeting meeting2 = Meeting.builder()
                    .title("철학하는 밤")
                    .bookTitle("니체의 말")
                    .bookAuthor("프리드리히 니체")
                    .bookCover("https://example.com/nietzsche.jpg")
                    .bookCategory("철학")
                    .startDate(LocalDateTime.of(2025, 6, 5, 19, 0))
                    .maxMembers(10)
                    .isActive(true)
                    .host(host)  // ✅ host 필드 설정

                    .build();

            meetingRepository.saveAll(List.of(meeting1, meeting2));
            System.out.println("더미 데이터 2건 추가 완료!");
        }

    }
}