package com.teamproject.TP_backend.repository;

import com.teamproject.TP_backend.domain.entity.DiscussionSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiscussionScheduleRepository extends JpaRepository<DiscussionSchedule, Long> {
    List<DiscussionSchedule> findByMeetingId(Long meetingId);
}
