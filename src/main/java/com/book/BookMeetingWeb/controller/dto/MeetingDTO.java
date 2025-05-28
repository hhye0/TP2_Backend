package com.book.BookMeetingWeb.controller.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingDTO {
    private String title;
    private String bookTitle;
    private String bookAuthor;
    private String bookCover;
    private String bookCategory;
    private LocalDate startDate;
    private LocalDate endDate;
    private int maxMembers;
}
