package com.teamproject.TP_backend.service;

import com.teamproject.TP_backend.config.security.CustomUserDetails;
import com.teamproject.TP_backend.domain.entity.User;
import com.teamproject.TP_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Spring Security에서 사용자 인증을 처리하기 위한 서비스
// - 로그인 시 입력한 이메일을 기준으로 사용자 정보를 로드
// - UserDetailsService 인터페이스 구현체로 동작
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

//     사용자 인증 시 이메일을 기반으로 사용자 정보를 조회
//     - 로그인 과정에서 호출
//     @param email 로그인 요청 시 사용자가 입력한 이메일
//     @return UserDetails (Spring Security에서 인증에 사용됨)
//     @throws UsernameNotFoundException 이메일이 존재하지 않을 경우
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + email));
        return new CustomUserDetails(user);
    }
}
