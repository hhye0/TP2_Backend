package com.teamproject.TP_backend.repository;

import com.teamproject.TP_backend.domain.entity.BookReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookReviewRepository extends JpaRepository<BookReview, Long> {
}
