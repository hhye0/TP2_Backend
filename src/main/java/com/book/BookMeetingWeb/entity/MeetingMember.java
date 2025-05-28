package com.book.BookMeetingWeb.entity;

import jakarta.persistence.*;
import lombok.*;
import com.book.BookMeetingWeb.entity.User;

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

    @Builder
    public static MeetingMember create(Meeting meeting, User user) {
        return MeetingMember.builder()
                .meeting(meeting)
                .user(user)
                .joinedAt(LocalDate.now())
                .build();
    }
}
