package com.book.BookMeetingWeb.runner;

import com.book.BookMeetingWeb.entity.Meeting;
import com.book.BookMeetingWeb.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final MeetingRepository meetingRepository;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            if (meetingRepository.count() == 0) {
                Meeting meeting1 = Meeting.builder()
                        .title("초기 독서 모임")
                        .bookTitle("데미안")
                        .bookAuthor("헤르만 헤세")
                        .bookCover("https://example.com/demian.jpg")
                        .bookCategory("고전")
                        .startDate(LocalDateTime.now().plusDays(1))
                        .endDate(LocalDateTime.now().plusDays(7))
                        .maxMembers(10)
                        .isActive(true)
                        .build();

                meetingRepository.save(meeting1);
                System.out.println("📚 초기 Meeting 데이터 삽입 완료!");
            }
        };
    }
}
