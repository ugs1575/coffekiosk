package com.coffeekiosk.coffeekiosk.service.notice;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coffeekiosk.coffeekiosk.common.exception.BusinessException;
import com.coffeekiosk.coffeekiosk.domain.notice.Notice;
import com.coffeekiosk.coffeekiosk.domain.notice.NoticeRepository;
import com.coffeekiosk.coffeekiosk.domain.user.User;
import com.coffeekiosk.coffeekiosk.domain.user.UserRepository;
import com.coffeekiosk.coffeekiosk.exception.ErrorCode;
import com.coffeekiosk.coffeekiosk.service.notice.request.NoticeSaveUpdateServiceRequest;
import com.coffeekiosk.coffeekiosk.service.notice.response.NoticeResponse;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class NoticeService {

	private final UserRepository userRepository;

	private final NoticeRepository noticeRepository;

	@Transactional
	public Long createNotice(Long userId, NoticeSaveUpdateServiceRequest request, LocalDateTime registeredDateTime) {
		User user = findUser(userId);

		Notice notice = request.toEntity(user, registeredDateTime);
		Notice savedNotice = noticeRepository.save(notice);

		return savedNotice.getId();
	}

	@Transactional
	public void updateNotice(Long noticeId, NoticeSaveUpdateServiceRequest request) {
		Notice notice = findNotice(noticeId);

		notice.update(request.getTitle(), request.getContent());
	}


	public NoticeResponse findById(Long noticeId) {
		Notice notice = findNotice(noticeId);
		return NoticeResponse.of(notice);
	}

	public List<NoticeResponse> findNotices() {
		List<Notice> notices = noticeRepository.findAll();
		return NoticeResponse.listOf(notices);
	}

	@Transactional
	public void delete(Long noticeId) {
		Notice notice = findNotice(noticeId);
		noticeRepository.deleteById(notice.getId());
	}

	private Notice findNotice(Long noticeId) {
		return noticeRepository.findByIdFetchJoin(noticeId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
	}

	private User findUser(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
	}

}