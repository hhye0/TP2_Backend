package com.teamproject.TP_backend.runner;

import com.teamproject.TP_backend.entity.User;
import com.teamproject.TP_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

@Component
@RequiredArgsConstructor
public class TestDataRunner implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!userRepository.existsByEmail("test@email.com")) {
            User user = User.builder()
                    .name("홍길동")
                    .email("test@email.com")
                    .password(passwordEncoder.encode("1234abcd!"))
                    .build();
            userRepository.save(user);
            System.out.println(">> 테스트 유저 생성 완료");
        }
    }
}