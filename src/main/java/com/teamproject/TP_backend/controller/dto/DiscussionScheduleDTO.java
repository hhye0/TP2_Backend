package com.teamproject.TP_backend.controller.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiscussionScheduleDTO {
    private Long id;
    private String title;
    private LocalDateTime dateTime;
    private String content;
}
