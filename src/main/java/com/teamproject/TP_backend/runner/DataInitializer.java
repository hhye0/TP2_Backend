package com.teamproject.TP_backend.runner;

import com.teamproject.TP_backend.entity.Meeting;
import com.teamproject.TP_backend.entity.User;
import com.teamproject.TP_backend.repository.MeetingRepository;
import com.teamproject.TP_backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(MeetingRepository meetingRepository, UserRepository userRepository) {
        return args -> {
            if (meetingRepository.count() == 0) {
                LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);

                // ID가 1인 유저가 있다고 가정
                User host = userRepository.findById(1L).orElseThrow(() ->
                        new IllegalStateException("ID가 1인 유저가 존재하지 않습니다."));

                Meeting meeting1 = Meeting.builder()
                        .title("초기 독서 모임")
                        .host(host)
                        .bookTitle("데미안")
                        .bookAuthor("헤르만 헤세")
                        .bookCover("https://example.com/demian.jpg")
                        .bookCategory("고전")
                        .startDate(tomorrow)
                        .maxMembers(10)
                        .isActive(true)
                        .build();

                meetingRepository.saveAll(List.of(meeting1));
                System.out.println("📚 초기 Meeting 데이터 삽입 완료!");
            }
        };
    }
}
