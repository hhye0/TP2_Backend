package com.teamproject.TP_backend.repository;

import com.teamproject.TP_backend.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

// User 엔티티에 대한 JPA Repository 인터페이스
// - 회원 조회, 중복 체크 등 사용자 관련 데이터 접근을
public interface UserRepository extends JpaRepository<User, Long> {

//     이메일을 기준으로 사용자 조회
//     - 로그인, 인증 토큰 발급 시 사용됨
//     @param email 사용자 이메일
//     @return 이메일에 해당하는 사용자 정보
    Optional<User> findByEmail(String email);

    Optional<User> findByName(String name);


//     해당 이메일이 이미 존재하는지 여부 확인
//     - 회원가입 시 중복 이메일 검사용
//     @param email 사용자 이메일
//     @return true면 이미 존재
    boolean existsByEmail(String email);

//     해당 닉네임이 이미 존재하는지 여부 확인
//     - 회원가입 시 중복 닉네임 검사용
//     @param nickname 사용자 닉네임
//     @return true면 이미 존재
    boolean existsByNickname(String nickname);

    Optional<User> findByNickname(String nickname);

    //fetch join
    @Query("select u from User u left join fetch u.hostedMeetings where u.id = :id")
    Optional<User> findWithHostedMeetingsById(@Param("id") Long id);

    @Query("select u from User u left join fetch u.meetingMemberships where u.id = :id")
    Optional<User> findWithMeetingMembershipsById(@Param("id") Long id);
}
