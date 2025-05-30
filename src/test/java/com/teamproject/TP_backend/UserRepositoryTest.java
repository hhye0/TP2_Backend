package com.teamproject.TP_backend;

import com.teamproject.TP_backend.domain.entity.User;
import com.teamproject.TP_backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testCreateUserIfNotExists() {
        String testEmail = "test@email.com";

        // 기존에 유저가 없으면 생성
        if (!userRepository.existsByEmail(testEmail)) {
            User user = User.builder()
                    .name("홍길동")
                    .email(testEmail)
                    .password(passwordEncoder.encode("1234abcd!"))
                    .build();
            userRepository.save(user);
        }

        // 저장 여부 확인
        User savedUser = userRepository.findByEmail(testEmail).orElse(null);
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getName()).isEqualTo("홍길동");
        // 비밀번호는 암호화 되었으므로 인코딩 전과 비교하지 않음
        assertThat(passwordEncoder.matches("1234abcd!", savedUser.getPassword())).isTrue();
    }
}