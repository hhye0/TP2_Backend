package com.teamproject.TP_backend.service;

import com.teamproject.TP_backend.config.security.JwtUtil;
import com.teamproject.TP_backend.controller.dto.*;
import com.teamproject.TP_backend.domain.entity.Meeting;
import com.teamproject.TP_backend.domain.entity.MeetingMember;
import com.teamproject.TP_backend.domain.entity.User;
import com.teamproject.TP_backend.domain.enums.ParticipationStatus;
import com.teamproject.TP_backend.domain.enums.UserRole;
import com.teamproject.TP_backend.exception.UserAlreadyExistsException;
import com.teamproject.TP_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// 사용자 인증 및 계정 관련 비즈니스 로직을 담당하는 서비스 클래스
// - 회원가입, 로그인, 정보 수정, 삭제 등을 처리
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    //     회원가입 처리
    //     - 이메일 중복 체크 후 유저 생성 및 저장
    //     @param dto 회원가입 요청 DTO
    //     @throws UserAlreadyExistsException 이메일 중복 시 예외 발생
    public void signup(SignupRequestDTO dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new UserAlreadyExistsException("이미 존재하는 이메일입니다.");
        }
        if (userRepository.existsByNickname(dto.nickName())){
            throw new UserAlreadyExistsException("이미 사용 중인 닉네임입니다.");
        }

        User user = User.builder()
                .name(dto.name())
                .nickname(dto.nickName())
                .email(dto.email())
                .password(passwordEncoder.encode(dto.password())) // 비밀번호 해싱
                .role(UserRole.USER) // 기본 역할 USER
                .build();


        userRepository.save(user);
    }

    //     로그인 처리
    //     - 이메일과 비밀번호 검증 후 JWT 토큰 발급
    //     @param dto 로그인 요청 DTO
    //     @return JWT 토큰 문자열
    public String login(LoginRequestDTO dto) {
        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> new RuntimeException("해당 이메일의 유저가 없습니다."));

        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        // JWT 토큰 생성 (이메일 + 역할 포함)
        return jwtUtil.createToken(user.getEmail(), user.getRole().name());
    }

    //     사용자 정보 수정
    //     - 이름, 이메일, 닉네임, 비밀번호 중 전달된 값만 변경
    //     @param id 수정 대상 사용자 ID
    //     @param dto 수정할 정보가 담긴 DTO
    //     @return true: 성공 / false: 유저 없음
    public boolean updateUser(Long id, UserUpdateRequestDTO dto) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return false;
        }

        User user = optionalUser.get();

        // 필드 별 null 검사 후 값 변경
        if (dto.getName() != null && !dto.getName().isEmpty()) {
            user.setName(dto.getName());
        }
        if (dto.getNickname() != null && !dto.getNickname().isEmpty()) {
            user.setNickname(dto.getNickname());
        }
        if (dto.getEmail() != null && !dto.getEmail().isEmpty()) {
            user.setEmail(dto.getEmail());
        }
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword())); // 비밀번호 해싱
        }

        userRepository.save(user);
        return true;
    }

    //     사용자 삭제 처리
    //     - 존재 여부 확인 후 삭제
    //     @param id 삭제할 사용자 ID
    //     @return true: 삭제 성공 / false: 존재하지 않음
    public boolean deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            return false;
        }
        userRepository.deleteById(id);
        return true;
    }

    // 사용자가 참여한 모임 목록 조회
    @Transactional(readOnly = true)
    public List<MeetingDTO> getJoinedMeetings(User user) {
        User userWithMemberships = userRepository.findWithMeetingMembershipsById(user.getId())
                .orElseThrow(() -> new RuntimeException("사용자 정보 없음"));

        return userWithMemberships.getMeetingMemberships().stream()
                .filter(member -> member.getStatus() == ParticipationStatus.APPROVED)
                .map(MeetingMember::getMeeting)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    //  현재 사용자가 호스트로 있는 모임 목록을 조회
    @Transactional(readOnly = true)
    public List<MeetingDTO> getHostedMeetings(User user) {
        User userWithMeetings = userRepository.findWithHostedMeetingsById(user.getId())
                .orElseThrow(() -> new RuntimeException("사용자 정보 없음"));

        return userWithMeetings.getHostedMeetings().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private MeetingDTO convertToDTO(Meeting meeting) {
        return MeetingDTO.builder()
                .id(meeting.getId())
                .title(meeting.getTitle())
                .bookTitle(meeting.getBookTitle())
                .bookAuthor(meeting.getBookAuthor())
                .bookCover(meeting.getBookCover())
                .bookCategory(meeting.getBookCategory())
                .startDate(meeting.getStartDate())
                .maxMembers(meeting.getMaxMembers())
                .active(meeting.isActive())
                .hostId(meeting.getHost().getId())
                .hostEmail(meeting.getHost().getEmail())
                .description(meeting.getDescription())
                // 참여자 리스트 변환
                .participants(
                        meeting.getParticipants().stream()
                                .map(participant -> ParticipantDTO.builder()
                                        .userId(participant.getUser().getId())
                                        .nickname(participant.getUser().getNickname())
                                        .status(participant.getStatus())
                                        .build())
                                .collect(Collectors.toList())
                )
                .build();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}