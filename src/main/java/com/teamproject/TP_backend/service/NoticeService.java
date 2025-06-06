package com.teamproject.TP_backend.service;

import com.teamproject.TP_backend.controller.dto.NoticeRequestDTO;
import com.teamproject.TP_backend.controller.dto.NoticeResponseDTO;
import com.teamproject.TP_backend.domain.entity.Meeting;
import com.teamproject.TP_backend.domain.entity.Notice;
import com.teamproject.TP_backend.domain.entity.User;
import com.teamproject.TP_backend.repository.MeetingRepository;
import com.teamproject.TP_backend.repository.NoticeRepository;
import com.teamproject.TP_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;

    // 공지사항 작성 - host만 작성 가능
    @Transactional
    public NoticeResponseDTO createNotice(NoticeRequestDTO dto, Long meetingId, String writerNickname) {
        User writer = userRepository.findByNickname(writerNickname)
                .orElseThrow(() -> new RuntimeException("작성자 유저를 찾을 수 없습니다."));

        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new RuntimeException("모임을 찾을 수 없습니다."));

        // 권한 체크: 작성자가 모임 호스트인지 확인
        if (!meeting.getHost().getId().equals(writer.getId())) {
            throw new RuntimeException("권한이 없습니다. 모임의 호스트만 공지사항을 작성할 수 있습니다.");
        }

        Notice notice = Notice.create(dto.getTitle(), dto.getContent(), writer, meeting);
        Notice saved = noticeRepository.save(notice);

        return new NoticeResponseDTO(
                saved.getId(),
                saved.getTitle(),
                saved.getContent(),
                saved.getWriter().getNickname()  // writer는 meeting.host이므로 닉네임 가져오기 가능
        );
    }

    // 모임별 공지사항 목록 조회
    @Transactional(readOnly = true)
    public List<NoticeResponseDTO> getNoticesByMeetingId(Long meetingId) {
        List<Notice> notices = noticeRepository.findByMeetingId(meetingId);
        return notices.stream()
                .map(n -> new NoticeResponseDTO(
                        n.getId(),
                        n.getTitle(),
                        n.getContent(),
                        n.getWriter().getNickname()
                ))
                .collect(Collectors.toList());
    }

    // 단일 공지사항 조회
    @Transactional(readOnly = true)
    public NoticeResponseDTO getNoticeById(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("공지사항을 찾을 수 없습니다."));
        return new NoticeResponseDTO(
                notice.getId(),
                notice.getTitle(),
                notice.getContent(),
                notice.getWriter().getNickname()
        );
    }

    // 공지사항 수정
    @Transactional
    public NoticeResponseDTO updateNotice(Long id, NoticeRequestDTO dto, String updaterNickname) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("공지사항을 찾을 수 없습니다."));

        if (!notice.getMeeting().getHost().getNickname().equals(updaterNickname)) {
            throw new RuntimeException("모임 호스트만 공지사항 수정이 가능합니다.");
        }

        notice.setTitle(dto.getTitle());
        notice.setContent(dto.getContent());

        return new NoticeResponseDTO(
                notice.getId(),
                notice.getTitle(),
                notice.getContent(),
                notice.getWriter().getNickname()
        );
    }

    // 공지사항 삭제
    @Transactional
    public void deleteNotice(Long id, String deleterNickname) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("공지사항을 찾을 수 없습니다."));

        if (!notice.getMeeting().getHost().getNickname().equals(deleterNickname)) {
            throw new RuntimeException("모임 호스트만 공지사항 삭제가 가능합니다.");
        }

        noticeRepository.delete(notice);
    }
}