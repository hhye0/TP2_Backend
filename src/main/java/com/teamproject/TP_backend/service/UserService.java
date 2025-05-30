package com.teamproject.TP_backend.service;

import com.teamproject.TP_backend.config.security.JwtUtil;
import com.teamproject.TP_backend.controller.dto.LoginRequestDTO;
import com.teamproject.TP_backend.controller.dto.SignupRequestDTO;
import com.teamproject.TP_backend.controller.dto.UserUpdateRequestDTO;
import com.teamproject.TP_backend.domain.entity.User;
import com.teamproject.TP_backend.domain.enums.UserRole;
import com.teamproject.TP_backend.exception.UserAlreadyExistsException;
import com.teamproject.TP_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public void signup(SignupRequestDTO dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new UserAlreadyExistsException("이미 존재하는 이메일입니다.");
        }

        User user = User.builder()
                .name(dto.name())
                .email(dto.email())
                .password(passwordEncoder.encode(dto.password()))
                .role(UserRole.USER) // 기본값 설정
                .build();


        userRepository.save(user);
    }

    public String login(LoginRequestDTO dto) {
        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> new RuntimeException("해당 이메일의 유저가 없습니다."));

        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        // JWT 토큰 생성
        return jwtUtil.createToken(user.getEmail(), user.getRole().name());
    }

    public boolean updateUser(Long id, UserUpdateRequestDTO dto) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return false;
        }

        User user = optionalUser.get();

        if (dto.getName() != null && !dto.getName().isEmpty()) {
            user.setName(dto.getName());
        }
        if (dto.getEmail() != null && !dto.getEmail().isEmpty()) {
            user.setEmail(dto.getEmail());
        }
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        userRepository.save(user);
        return true;
    }

    public boolean deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            return false;
        }
        userRepository.deleteById(id);
        return true;
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}