package com.teamproject.TP_backend.repository;

import com.teamproject.TP_backend.domain.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
