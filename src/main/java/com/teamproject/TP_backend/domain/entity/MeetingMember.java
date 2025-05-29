package com.teamproject.TP_backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(
        name = "meeting_members",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"meeting_id", "user_id"})
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
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id", nullable = false)
    private Meeting meeting;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "joined_at", nullable = false)
    private LocalDate joinedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ParticipationStatus status;

    @Builder
    public static MeetingMember create(Meeting meeting, User user) {
        return MeetingMember.builder()
                .meeting(meeting)
                .user(user)
                .joinedAt(LocalDate.now())
                .status(ParticipationStatus.PENDING)
                .build();
    }

    public enum ParticipationStatus {
        PENDING,  // 신청 상태
        APPROVED, // 수락됨
        REJECTED  // 거절됨
    }

}
