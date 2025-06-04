package com.teamproject.TP_backend.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class DiscussionScheduleCreateDTO {

    @NotNull
    private Long meetingId;

    @NotNull
    private LocalDate date;

    @NotNull
    private LocalTime time;

    @NotNull
    private String topic;

    private String memo;

    // Getters and Setters
}
