package com.teamproject.TP_backend.controller.dto;

import com.teamproject.TP_backend.domain.entity.User;
import lombok.Builder;
import lombok.Data;

// 사용자 정보 조회 응답에 사용되는 DTO
// 예: 마이페이지, /users/me 등에서 로그인한 사용자 정보를 반환할 때 사용
@Data
@Builder
public class UserDTO {

    private Long id;      // 사용자 ID
    private String name;  // 사용자 이름
    private String email; // 사용자 이메일

//     User 엔티티를 UserDTO로 변환하는 정적 메서드
//     @param user 엔티티 객체
//     @return UserDTO로 변환된 객체
    public static UserDTO from(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
