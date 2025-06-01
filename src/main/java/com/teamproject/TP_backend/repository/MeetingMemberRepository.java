package com.teamproject.TP_backend.repository;

import com.teamproject.TP_backend.domain.entity.Meeting;
import com.teamproject.TP_backend.domain.entity.MeetingMember;
import com.teamproject.TP_backend.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// MeetingMember 엔티티에 대한 JPA Repository 인터페이스
// - 모임 참여 여부 확인, 특정 사용자-모임 간의 참여 정보 조회 등을 지원
public interface MeetingMemberRepository extends JpaRepository<MeetingMember, Long> {

//     해당 사용자가 특정 모임에 이미 참여 중인지 확인
//     @param meeting 모임
//     @param user 사용자
//     @return true면 이미 참여 중
    boolean existsByMeetingAndUser(Meeting meeting, User user);

//     특정 모임과 사용자에 대한 참여 정보 조회
//     @param meetingId 모임 ID
//     @param userId 사용자 ID
//     @return 참여 정보가 존재하면 Optional로 반환
    Optional<MeetingMember> findByMeetingIdAndUserId(Long meetingId, Long userId);
}
