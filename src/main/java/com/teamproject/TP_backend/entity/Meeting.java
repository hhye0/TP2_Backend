package com.teamproject.TP_backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "meetings")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(name = "book_title", nullable = false, length = 100)
    private String bookTitle;

    @Column(name = "book_author", nullable = false, length = 100)
    private String bookAuthor;

    @Column(name = "book_cover", length = 255)
    private String bookCover;

    @Column(name = "book_category", length = 100)
    private String bookCategory;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "max_members", nullable = false)
    private int maxMembers;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // 호스트(생성자)와의 관계 추가
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id", nullable = false)
    private User host;

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MeetingMember> participants = new ArrayList<>();

    @Builder
    public Meeting(String title, String bookTitle, String bookAuthor, String bookCover,
                   String bookCategory, LocalDateTime startDate,
                   int maxMembers, boolean isActive, User host) {
        this.title = title;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.bookCover = bookCover;
        this.bookCategory = bookCategory;
        this.startDate = startDate;
        this.maxMembers = maxMembers;
        this.isActive = isActive;
        this.host =host;
    }

    public void update(String title, String bookTitle, String bookAuthor, String bookCover,
                       String bookCategory, LocalDateTime startDate,
                       int maxMembers, User host) {
        this.title = title;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.bookCover = bookCover;
        this.bookCategory = bookCategory;
        this.startDate = startDate;
        this.maxMembers = maxMembers;
        this.host = host;
    }
}
