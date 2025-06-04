package com.teamproject.TP_backend.config.security;

import com.teamproject.TP_backend.domain.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

// Spring Security의 UserDetails를 구현한 사용자 정의 클래스
// JWT 인증 이후 SecurityContext에 저장되는 사용자 정보 표현
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 현재는 권한 없음
        return Collections.singletonList(() -> "ROLE_" + user.getRole());
    }

    @Override
    public String getPassword() {
        return user.getPassword(); // 비밀번호 반환
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // 로그인 ID = 이메일
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정이 만료되지 않았는가?
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정이 잠기지 않았는가?
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 비밀번호가 만료되지 않았는가?
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정이 활성화되어 있는가?
    }

    public User getUser() {
        return user;
    } // 로그인된 사용자
}