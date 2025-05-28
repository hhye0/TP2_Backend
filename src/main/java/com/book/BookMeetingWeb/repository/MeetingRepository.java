package com.book.BookMeetingWeb.repository;

import com.book.BookMeetingWeb.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
}
