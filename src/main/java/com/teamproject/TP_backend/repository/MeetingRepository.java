package com.teamproject.TP_backend.repository;

import com.teamproject.TP_backend.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
}
