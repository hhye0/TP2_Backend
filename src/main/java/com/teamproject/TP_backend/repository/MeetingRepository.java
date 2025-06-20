package com.teamproject.TP_backend.repository;

import com.teamproject.TP_backend.domain.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Meeting 엔티티에 대한 JPA Repository 인터페이스
// - 기본 CRUD 기능 (findById, save, delete 등)을 제공
// - 추가적인 쿼리가 필요할 경우 메서드 정의를 통해 확장 가능
public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    // 현재는 기본 CRUD 기능만 사용

    // title과 host.nickname을 포함 검색 (대소문자 구분 없이)
    List<Meeting> findByTitleContainingIgnoreCaseAndHost_NicknameContainingIgnoreCase(String title, String hostNickname);
    List<Meeting> findByTitleContainingIgnoreCase(String title);
    List<Meeting> findByHost_NicknameContainingIgnoreCase(String nickname);

}
