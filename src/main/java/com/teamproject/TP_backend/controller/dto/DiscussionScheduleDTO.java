package com.teamproject.TP_backend.controller.dto;

import com.teamproject.TP_backend.domain.entity.DiscussionSchedule;

import java.time.LocalDate;
import java.time.LocalTime;

public record DiscussionScheduleDTO(
        Long id,
        String topic,
        LocalDate date,
        LocalTime time,
        String memo,
        Long meetingId
) {
    public static DiscussionScheduleDTO fromEntity(DiscussionSchedule entity) {
        return new DiscussionScheduleDTO(
                entity.getId(),
                entity.getTopic(),
                entity.getDate(),
                entity.getTime(),
                entity.getMemo(),
                entity.getMeeting().getId()
        );
    }
}
