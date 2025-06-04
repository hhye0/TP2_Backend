package com.teamproject.TP_backend.domain.entity;

import com.teamproject.TP_backend.domain.enums.GroupRole;
import jakarta.persistence.*;
import lombok.*;
import com.teamproject.TP_backend.domain.enums.ParticipationStatus;

import java.time.LocalDate;

// 모임과 사용자 간의 참여 관계를 나타내는 엔티티
// - 어떤 유저가 어떤 모임에 참여 중인지, 상태는 어떤지를 기록
@Entity
@Table(
        name = "meeting_members",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"meeting_id", "user_id"}) // 동일 모임 중복 참여 방지
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 참여 고유 ID (PK)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id", nullable = false)
    private Meeting meeting; // 참여한 모임

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 참여자

    @Column(name = "joined_at", nullable = false)
    private LocalDate joinedAt; // 참여 신청 날짜

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ParticipationStatus status; // 참여 상태 (대기/승인/거절)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GroupRole role;

    //     정적 팩토리 메서드로 기본 참여 정보 생성
    //     - 상태는 기본값으로 PENDING
    public static MeetingMember create(Meeting meeting, User user) {
        return MeetingMember.builder()
                .meeting(meeting)
                .user(user)
                .joinedAt(LocalDate.now())
                .status(ParticipationStatus.PENDING)
                .role(GroupRole.MEMBER)
                .build();
    }

    public boolean isAccepted() {
        return this.status == ParticipationStatus.APPROVED;
    }


}
