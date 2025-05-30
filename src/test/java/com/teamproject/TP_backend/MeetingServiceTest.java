package com.teamproject.TP_backend;

import com.teamproject.TP_backend.controller.dto.MeetingDTO;
import com.teamproject.TP_backend.domain.entity.Meeting;
import com.teamproject.TP_backend.domain.entity.User;
import com.teamproject.TP_backend.domain.enums.UserRole;
import com.teamproject.TP_backend.repository.MeetingRepository;
import com.teamproject.TP_backend.service.MeetingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

// MeetingService 내의 비즈니스 로직 테스트
@ExtendWith(MockitoExtension.class)
class MeetingServiceTest {

    @Mock
    private MeetingRepository meetingRepository;

    // MeetingService는 UserRepository를 직접 사용하지 않고 User 객체를 매개변수로 받으므로
    // UserRepository 모의 객체는 여기서는 필수가 아닙니다.
    // 만약 MeetingService 내부에서 UserRepository를 호출하는 로직이 있다면 추가해야 합니다.

    @InjectMocks
    private MeetingService meetingService;

    private User hostUser;
    private Meeting meeting1;
    private Meeting meeting2;
    private MeetingDTO meetingDTO1;

    @BeforeEach
    void setUp() {
        hostUser = User.builder()
                .id(1L)
                .name("Host User")
                .email("host@example.com")
                .password("password")
                .role(UserRole.USER)
                .build();

        meeting1 = Meeting.builder()
                .title("Test Meeting 1")
                .bookTitle("Book 1")
                .bookAuthor("Author 1")
                .startDate(LocalDateTime.now().plusDays(1))
                .maxMembers(10)
                .host(hostUser)
                .isActive(true)
                .build();
        // Meeting 엔티티에 ID가 설정되어야 toDTO 변환 시 hostId 등을 정상적으로 가져올 수 있습니다.
        // 실제로는 DB에서 조회 시 ID가 자동 할당되지만, 테스트에서는 수동으로 설정하거나
        // save 모의 호출 시 반환 객체에 ID를 포함하도록 합니다.
        // 여기서는 편의상 빌더에 ID를 추가했다고 가정하거나, toDTO가 ID 없이도 동작하도록 DTO 필드 조정 필요.
        // 현재 Meeting 엔티티에는 ID 자동 생성이지만, 테스트 객체 생성 시에는 ID를 명시적으로 넣어주면 좋습니다.
        meeting1.setId(1L); // ID 설정

        meeting2 = Meeting.builder()
                .title("Test Meeting 2")
                .bookTitle("Book 2")
                .bookAuthor("Author 2")
                .startDate(LocalDateTime.now().plusDays(2))
                .maxMembers(5)
                .host(hostUser)
                .isActive(true)
                .build();
        meeting2.setId(2L); // ID 설정

        meetingDTO1 = MeetingDTO.builder()
                .id(1L)
                .title("Test Meeting DTO 1")
                .bookTitle("Book DTO 1")
                .bookAuthor("Author DTO 1")
                .startDate(LocalDateTime.now().plusDays(5))
                .maxMembers(10)
                .hostId(hostUser.getId())
                .active(true)
                .build();
    }

    @Test
    @DisplayName("모든 모임 조회 시 DTO 리스트 반환")
    void getAllMeetings_ShouldReturnListOfMeetingDTOs() {
        // given
        given(meetingRepository.findAll()).willReturn(List.of(meeting1, meeting2));

        // when
        List<MeetingDTO> result = meetingService.getAllMeetings();

        // then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getTitle()).isEqualTo(meeting1.getTitle());
        assertThat(result.get(1).getTitle()).isEqualTo(meeting2.getTitle());
        verify(meetingRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("ID로 모임 조회 성공 시 DTO 반환")
    void getMeeting_WhenMeetingExists_ShouldReturnMeetingDTO() {
        // given
        Long meetingId = 1L;
        given(meetingRepository.findById(meetingId)).willReturn(Optional.of(meeting1));

        // when
        MeetingDTO result = meetingService.getMeeting(meetingId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(meeting1.getId());
        assertThat(result.getTitle()).isEqualTo(meeting1.getTitle());
        verify(meetingRepository, times(1)).findById(meetingId);
    }

    @Test
    @DisplayName("존재하지 않는 ID로 모임 조회 시 예외 발생")
    void getMeeting_WhenMeetingDoesNotExist_ShouldThrowRuntimeException() {
        // given
        Long meetingId = 99L;
        given(meetingRepository.findById(meetingId)).willReturn(Optional.empty());

        // when & then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            meetingService.getMeeting(meetingId);
        });
        assertThat(exception.getMessage()).isEqualTo("모임을 찾을 수 없습니다.");
        verify(meetingRepository, times(1)).findById(meetingId);
    }

    @Test
    @DisplayName("모임 생성 성공 시 생성된 모임 DTO 반환")
    void createMeeting_ShouldReturnCreatedMeetingDTO() {
        // given
        // createMeeting의 Meeting.builder()에서 host를 user로 설정하므로,
        // 저장될 meeting 객체는 meetingDTO1의 정보와 hostUser 정보를 가질 것입니다.
        // toDTO 변환 시 ID가 필요하므로, 모의 save 호출 시 ID가 설정된 객체를 반환하도록 합니다.
        Meeting savedMeeting = Meeting.builder()
                .title(meetingDTO1.getTitle())
                .host(hostUser)
                .bookTitle(meetingDTO1.getBookTitle())
                .bookAuthor(meetingDTO1.getBookAuthor())
                .bookCover(meetingDTO1.getBookCover())
                .bookCategory(meetingDTO1.getBookCategory())
                .startDate(meetingDTO1.getStartDate())
                .maxMembers(meetingDTO1.getMaxMembers())
                .isActive(true)
                .build();
        savedMeeting.setId(meetingDTO1.getId()); // 모의 save 시 반환될 객체에 ID 설정

        given(meetingRepository.save(any(Meeting.class))).willReturn(savedMeeting);

        // when
        MeetingDTO result = meetingService.createMeeting(meetingDTO1, hostUser);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo(meetingDTO1.getTitle());
        assertThat(result.getHostId()).isEqualTo(hostUser.getId());
        // 실제 저장되는 객체는 입력 DTO와 User를 기반으로 만들어지므로,
        // ArgumentCaptor를 사용해 저장되는 Meeting 객체를 검증할 수도 있습니다.
        verify(meetingRepository, times(1)).save(any(Meeting.class));
    }

    @Test
    @DisplayName("모임 수정 성공 시 업데이트된 모임 DTO 반환 (호스트 일치)")
    void updateMeeting_WhenUserIsHost_ShouldReturnUpdatedMeetingDTO() {
        // given
        Long meetingId = 1L;
        MeetingDTO updateRequestDTO = MeetingDTO.builder()
                .title("Updated Title")
                .bookTitle("Updated Book")
                .bookAuthor("Updated Author")
                .startDate(LocalDateTime.now().plusDays(10))
                .maxMembers(12)
                .build();

        // 기존 모임 설정 (meeting1은 hostUser가 호스트)
        given(meetingRepository.findById(meetingId)).willReturn(Optional.of(meeting1));

        // save 호출 시 업데이트된 내용을 반영한 Meeting 객체가 반환된다고 가정
        Meeting updatedMeetingEntity = Meeting.builder()
                .title(updateRequestDTO.getTitle())
                .host(hostUser) // 호스트는 유지
                .bookTitle(updateRequestDTO.getBookTitle())
                .bookAuthor(updateRequestDTO.getBookAuthor())
                .startDate(updateRequestDTO.getStartDate())
                .maxMembers(updateRequestDTO.getMaxMembers())
                .isActive(meeting1.isActive()) // isActive는 DTO에 없으므로 기존 값 유지 가정
                .build();
        updatedMeetingEntity.setId(meetingId);
        updatedMeetingEntity.setCreatedAt(meeting1.getCreatedAt()); // 생성일 유지

        given(meetingRepository.save(any(Meeting.class))).willReturn(updatedMeetingEntity);


        // when
        MeetingDTO result = meetingService.updateMeeting(meetingId, updateRequestDTO, hostUser);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(meetingId);
        assertThat(result.getTitle()).isEqualTo(updateRequestDTO.getTitle());
        assertThat(result.getBookTitle()).isEqualTo(updateRequestDTO.getBookTitle());
        verify(meetingRepository, times(1)).findById(meetingId);
        verify(meetingRepository, times(1)).save(any(Meeting.class));
    }

    @Test
    @DisplayName("모임 수정 시 호스트 불일치로 예외 발생")
    void updateMeeting_WhenUserIsNotHost_ShouldThrowRuntimeException() {
        // given
        Long meetingId = 1L;
        User anotherUser = User.builder().id(2L).name("Another User").build();
        MeetingDTO updateRequestDTO = MeetingDTO.builder().title("Updated Title").build();

        given(meetingRepository.findById(meetingId)).willReturn(Optional.of(meeting1)); // meeting1의 호스트는 hostUser(ID: 1L)

        // when & then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            meetingService.updateMeeting(meetingId, updateRequestDTO, anotherUser); // anotherUser(ID: 2L)로 수정 시도
        });
        assertThat(exception.getMessage()).isEqualTo("권한이 없습니다.");
        verify(meetingRepository, times(1)).findById(meetingId);
        verify(meetingRepository, never()).save(any(Meeting.class));
    }

    @Test
    @DisplayName("존재하지 않는 모임 수정 시 예외 발생")
    void updateMeeting_WhenMeetingDoesNotExist_ShouldThrowRuntimeException() {
        // given
        Long meetingId = 99L;
        MeetingDTO updateRequestDTO = MeetingDTO.builder().title("Updated Title").build();
        given(meetingRepository.findById(meetingId)).willReturn(Optional.empty());

        // when & then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            meetingService.updateMeeting(meetingId, updateRequestDTO, hostUser);
        });
        assertThat(exception.getMessage()).isEqualTo("모임을 찾을 수 없습니다.");
        verify(meetingRepository, times(1)).findById(meetingId);
        verify(meetingRepository, never()).save(any(Meeting.class));
    }

    @Test
    @DisplayName("모임 삭제 (권한 검사 없음 - 현재 서비스 로직 기준)")
    void deleteMeeting_ShouldCallRepositoryDeleteById() {
        // given
        Long meetingId = 1L;
        // MeetingService의 deleteMeeting은 현재 User를 사용한 권한 검사를 하지 않음.
        // 따라서 어떤 User 객체를 전달하든 deleteById가 호출되어야 함.
        // 만약 서비스 레이어에서 권한 검사가 추가된다면, 이 테스트는 수정되어야 함.
        // (예: findById로 모임을 가져와 호스트를 확인하는 로직 추가 시)

        // 현재 MeetingService.deleteMeeting(Long id, User user)는 다음과 같음:
        // public void deleteMeeting(Long id, User user) {
        //    meetingRepository.deleteById(id);
        // }
        // 만약 모임 존재 여부나 호스트 권한을 확인해야 한다면 MeetingRepository.findById() 등을 먼저 호출하도록 로직이 변경되어야 함.
        // 현재 로직에서는 deleteById만 호출되므로, 해당 호출 여부만 검증.

        // when
        meetingService.deleteMeeting(meetingId, hostUser);

        // then
        verify(meetingRepository, times(1)).deleteById(meetingId);
    }
}