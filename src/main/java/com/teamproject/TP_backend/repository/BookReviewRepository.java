package com.teamproject.TP_backend.repository;

import com.teamproject.TP_backend.domain.entity.BookReview;
import com.teamproject.TP_backend.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookReviewRepository extends JpaRepository<BookReview, Long> {
    Page<BookReview> findByReviewer(User user, Pageable pageable);
}
