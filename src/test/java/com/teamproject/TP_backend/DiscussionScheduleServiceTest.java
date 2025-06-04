package com.teamproject.TP_backend;

import com.teamproject.TP_backend.domain.entity.Meeting;
import com.teamproject.TP_backend.domain.entity.MeetingMember;
import com.teamproject.TP_backend.domain.entity.User;
import com.teamproject.TP_backend.domain.entity.*;
import com.teamproject.TP_backend.domain.enums.GroupRole;
import com.teamproject.TP_backend.domain.enums.ParticipationStatus;
import com.teamproject.TP_backend.repository.*;
import com.teamproject.TP_backend.service.DiscussionScheduleService;
import com.teamproject.TP_backend.controller.dto.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class DiscussionScheduleServiceTest {

    @Autowired
    private DiscussionScheduleService discussionScheduleService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private MeetingMemberRepository meetingMemberRepository;

    @Autowired
    private DiscussionScheduleRepository discussionScheduleRepository;

    private User host;
    private Meeting meeting;

    @BeforeEach
    void setUp() {
        host = userRepository.save(User.builder()
                .email("host@example.com")
                .nickname("Host")
                .build());

        meeting = meetingRepository.save(Meeting.builder()
                .title("테스트 모임")
                .host(host)
                .bookTitle("Test Book")
                .bookAuthor("Test Author")
                .startDate(LocalDate.now().atStartOfDay())
                .isActive(true)
                .maxMembers(10)
                .build());

        meetingMemberRepository.save(MeetingMember.builder()
                .user(host)
                .meeting(meeting)
                .role(GroupRole.HOST)
                .status(ParticipationStatus.APPROVED)
                .joinedAt(LocalDate.now())
                .build());
    }

    @Test
    @DisplayName("[1] 호스트가 토론 일정을 정상적으로 생성한다")
    void testCreateSchedule_HostSuccess() {
        DiscussionScheduleCreateDTO dto = new DiscussionScheduleCreateDTO();
        dto.setMeetingId(meeting.getId());
        dto.setDate(LocalDate.of(2025, 6, 15));
        dto.setTime(LocalTime.of(20, 0));
        dto.setTopic("책 주제에 대해 토론");
        dto.setMemo("간단한 메모");

        DiscussionSchedule schedule = discussionScheduleService.createSchedule(dto, host);

        assertThat(schedule).isNotNull();
        assertThat(schedule.getMeeting().getId()).isEqualTo(meeting.getId());
        assertThat(schedule.getTopic()).isEqualTo("책 주제에 대해 토론");
    }

    @Test
    @DisplayName("[2] 비회원은 토론 일정을 생성할 수 없다")
    void testCreateSchedule_NonMemberFail() {
        User outsider = userRepository.save(User.builder()
                .email("outsider@example.com")
                .nickname("Outsider")
                .build());

        DiscussionScheduleCreateDTO dto = new DiscussionScheduleCreateDTO();
        dto.setMeetingId(meeting.getId());
        dto.setDate(LocalDate.of(2025, 6, 16));
        dto.setTime(LocalTime.of(19, 30));
        dto.setTopic("외부자 접근 시도");

        assertThatThrownBy(() -> discussionScheduleService.createSchedule(dto, outsider))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("모임의 멤버가 아닙니다");
    }

    @Test
    @DisplayName("[3] 호스트가 아닌 멤버는 토론 일정을 생성할 수 없다")
    void testCreateSchedule_NonHostMemberFail() {
        User member = userRepository.save(User.builder()
                .email("member@example.com")
                .nickname("Member")
                .build());

        meetingMemberRepository.save(MeetingMember.builder()
                .user(member)
                .meeting(meeting)
                .role(MeetingRole.MEMBER)
                .status(MemberStatus.APPROVED)
                .joinedAt(LocalDate.now())
                .build());

        DiscussionScheduleCreateDTO dto = new DiscussionScheduleCreateDTO();
        dto.setMeetingId(meeting.getId());
        dto.setDate(LocalDate.of(2025, 6, 17));
        dto.setTime(LocalTime.of(18, 0));
        dto.setTopic("멤버는 생성할 수 없음");

        assertThatThrownBy(() -> discussionScheduleService.createSchedule(dto, member))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("호스트만 생성할 수 있습니다");
    }

    @Test
    @DisplayName("[4] 모임이 존재하지 않으면 예외 발생")
    void testCreateSchedule_InvalidMeetingId() {
        DiscussionScheduleCreateDTO dto = new DiscussionScheduleCreateDTO();
        dto.setMeetingId(99999L);  // 존재하지 않는 ID
        dto.setDate(LocalDate.of(2025, 6, 18));
        dto.setTime(LocalTime.of(19, 0));
        dto.setTopic("유효하지 않은 모임");

        assertThatThrownBy(() -> discussionScheduleService.createSchedule(dto, host))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 모임을 찾을 수 없습니다");
    }

    @Test
    @DisplayName("[5] 이미 일정이 있는 날짜+시간에는 생성 불가")
    void testCreateSchedule_DuplicateDateTime() {
        DiscussionScheduleCreateDTO dto = new DiscussionScheduleCreateDTO();
        dto.setMeetingId(meeting.getId());
        dto.setDate(LocalDate.of(2025, 6, 19));
        dto.setTime(LocalTime.of(21, 0));
        dto.setTopic("첫 일정");

        discussionScheduleService.createSchedule(dto, host);

        DiscussionScheduleCreateDTO duplicate = new DiscussionScheduleCreateDTO();
        duplicate.setMeetingId(meeting.getId());
        duplicate.setDate(LocalDate.of(2025, 6, 19));
        duplicate.setTime(LocalTime.of(21, 0));
        duplicate.setTopic("중복 일정");

        assertThatThrownBy(() -> discussionScheduleService.createSchedule(duplicate, host))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 해당 시간에 일정이 존재합니다");
    }

    @Test
    @DisplayName("[6] 비활성화된 모임에 대해서는 생성 불가")
    void testCreateSchedule_InactiveMeeting() {
        meeting.setIsActive(false);
        meetingRepository.save(meeting);

        DiscussionScheduleCreateDTO dto = new DiscussionScheduleCreateDTO();
        dto.setMeetingId(meeting.getId());
        dto.setDate(LocalDate.of(2025, 6, 20));
        dto.setTime(LocalTime.of(22, 0));
        dto.setTopic("비활성 모임");

        assertThatThrownBy(() -> discussionScheduleService.createSchedule(dto, host))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("비활성화된 모임입니다");
    }

    @Test
    @DisplayName("[7] 날짜와 시간이 null이면 예외 발생")
    void testCreateSchedule_NullDateTime() {
        DiscussionScheduleCreateDTO dto = new DiscussionScheduleCreateDTO();
        dto.setMeetingId(meeting.getId());
        dto.setTopic("시간 없음");

        assertThatThrownBy(() -> discussionScheduleService.createSchedule(dto, host))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("날짜와 시간은 필수입니다");
    }
}
