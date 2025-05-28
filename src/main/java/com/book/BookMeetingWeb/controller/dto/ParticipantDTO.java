package com.book.BookMeetingWeb.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParticipantDTO {
    private Long userId;
    private String userName;
    private String status;
}
