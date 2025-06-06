package com.teamproject.TP_backend.runner;

import com.teamproject.TP_backend.domain.entity.User;
import com.teamproject.TP_backend.domain.enums.UserRole;
import com.teamproject.TP_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestDataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByName("admin").isEmpty()) {
            User admin = User.builder()
                    .name("admin")
                    .password(passwordEncoder.encode("admin1234"))
                    .role(UserRole.ADMIN)
                    .email("admin@example.com")
                    .nickname("관리자")
                    .build();

            userRepository.save(admin);
        }
    }
}
