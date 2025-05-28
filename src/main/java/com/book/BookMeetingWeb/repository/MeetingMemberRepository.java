package com.book.BookMeetingWeb.repository;

import com.book.BookMeetingWeb.entity.Meeting;
import com.book.BookMeetingWeb.entity.MeetingMember;
import com.book.BookMeetingWeb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MeetingMemberRepository extends JpaRepository<MeetingMember, Long> {
    boolean existsByMeetingAndUser(Meeting meeting, User user);
    Optional<MeetingMember> findByMeetingIdAndUserId(Long meetingId, Long userId);

}
