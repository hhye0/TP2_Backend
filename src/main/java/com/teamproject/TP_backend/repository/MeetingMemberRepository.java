package com.teamproject.TP_backend.repository;

import com.teamproject.TP_backend.entity.Meeting;
import com.teamproject.TP_backend.entity.MeetingMember;
import com.teamproject.TP_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MeetingMemberRepository extends JpaRepository<MeetingMember, Long> {
    boolean existsByMeetingAndUser(Meeting meeting, User user);
    Optional<MeetingMember> findByMeetingIdAndUserId(Long meetingId, Long userId);

}
