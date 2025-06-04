package com.teamproject.TP_backend.service;

import com.teamproject.TP_backend.controller.dto.NoticeRequestDTO;
import com.teamproject.TP_backend.controller.dto.NoticeResponseDTO;
import com.teamproject.TP_backend.domain.entity.Notice;
import com.teamproject.TP_backend.domain.entity.User;
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
    private final UserRepository userRepository;

    // 공지사항 작성
    @Transactional
    public NoticeResponseDTO createNotice(NoticeRequestDTO dto, String authorNickname) {
        User author = userRepository.findByNickname(authorNickname)
                .orElseThrow(() -> new RuntimeException("작성자 유저를 찾을 수 없습니다."));

        Notice notice = Notice.create(dto.getTitle(), dto.getContent(), author);
        Notice saved = noticeRepository.save(notice);

        return new NoticeResponseDTO(
                saved.getId(),
                saved.getTitle(),
                saved.getContent(),
                saved.getAuthor().getNickname()
        );
    }

    // 공지사항 전체 리스트 조회
    @Transactional(readOnly = true)
    public List<NoticeResponseDTO> getAllNotices() {
        return noticeRepository.findAll().stream()
                .map(n -> new NoticeResponseDTO(
                        n.getId(),
                        n.getTitle(),
                        n.getContent(),
                        n.getAuthor().getNickname()
                ))
                .collect(Collectors.toList());
    }

    // 공지사항 단건 조회
    @Transactional(readOnly = true)
    public NoticeResponseDTO getNoticeById(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("공지사항을 찾을 수 없습니다."));
        return new NoticeResponseDTO(
                notice.getId(),
                notice.getTitle(),
                notice.getContent(),
                notice.getAuthor().getNickname()
        );
    }

    // 공지사항 수정
    @Transactional
    public NoticeResponseDTO updateNotice(Long id, NoticeRequestDTO dto, String authorNickname) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("공지사항을 찾을 수 없습니다."));

        if (!notice.getAuthor().getNickname().equals(authorNickname)) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }

        notice.setTitle(dto.getTitle());
        notice.setContent(dto.getContent());

        return new NoticeResponseDTO(
                notice.getId(),
                notice.getTitle(),
                notice.getContent(),
                notice.getAuthor().getNickname()
        );
    }

    // 공지사항 삭제
    @Transactional
    public void deleteNotice(Long id, String authorNickname) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("공지사항을 찾을 수 없습니다."));

        if (!notice.getAuthor().getNickname().equals(authorNickname)) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        noticeRepository.delete(notice);
    }

}