package com.book.BookMeetingWeb.runner;

import com.book.BookMeetingWeb.entity.Meeting;
import com.book.BookMeetingWeb.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TestDataRunner implements CommandLineRunner {

    private final MeetingRepository meetingRepository;

    @Override
    public void run(String... args) {
        if (meetingRepository.count() == 0) {
            Meeting meeting1 = Meeting.builder()
                    .title("햄과 함께하는 심리학 독서모임")
                    .bookTitle("자기 이해의 심리학")
                    .bookAuthor("이민규")
                    .bookCover("https://example.com/psychology.jpg")
                    .bookCategory("심리학")
                    .startDate(LocalDateTime.of(2025, 6, 10, 10, 0))
                    .endDate(LocalDateTime.of(2025, 6, 24, 10, 0))
                    .maxMembers(15)
                    .isActive(true)
                    .build();

            Meeting meeting2 = Meeting.builder()
                    .title("햄과 철학하는 밤")
                    .bookTitle("니체의 말")
                    .bookAuthor("프리드리히 니체")
                    .bookCover("https://example.com/nietzsche.jpg")
                    .bookCategory("철학")
                    .startDate(LocalDateTime.of(2025, 6, 5, 19, 0))
                    .endDate(LocalDateTime.of(2025, 6, 20, 21, 0))
                    .maxMembers(10)
                    .isActive(true)
                    .build();

            meetingRepository.saveAll(List.of(meeting1, meeting2));
            System.out.println("🎉 더미 데이터 2건 추가 완료!");
        }
    }
}
