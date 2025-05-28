package com.teamproject.TP_backend.repository;

import com.teamproject.TP_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // 이메일이나 닉네임 등으로 조회하는 메서드도 추가 가능
    // Optional<User> findByEmail(String email);
}
