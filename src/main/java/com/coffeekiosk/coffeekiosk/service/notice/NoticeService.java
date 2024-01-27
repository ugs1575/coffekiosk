package com.coffeekiosk.coffeekiosk.service.notice;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
import lombok.extern.slf4j.Slf4j;

@CacheConfig(cacheNames = "notices")
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class NoticeService {

	private final UserRepository userRepository;

	private final NoticeRepository noticeRepository;

	@CacheEvict(key = "'all'")
	@Transactional
	public NoticeResponse createNotice(Long userId, NoticeSaveUpdateServiceRequest request, LocalDateTime registeredDateTime) {
		User user = findUser(userId);

		Notice notice = request.toEntity(user, registeredDateTime);
		Notice savedNotice = noticeRepository.save(notice);

		return NoticeResponse.of(savedNotice);
	}

	@CachePut(key = "#noticeId")
	@CacheEvict(key = "'all'")
	@Transactional
	public NoticeResponse updateNotice(Long noticeId, NoticeSaveUpdateServiceRequest request) {
		Notice notice = findNotice(noticeId);

		notice.update(request.getTitle(), request.getContent());

		return NoticeResponse.of(notice);
	}

	@Cacheable(key = "#noticeId", unless = "#result == null")
	public NoticeResponse findById(Long noticeId) {
		Notice notice = findNotice(noticeId);
		return NoticeResponse.of(notice);
	}

	@Cacheable(key = "'all'")
	public List<NoticeResponse> findNotices() {
		List<Notice> notices = noticeRepository.findAll();
		return NoticeResponse.listOf(notices);
	}

	@Caching(evict = {
		@CacheEvict(key = "'all'"),
		@CacheEvict(key = "#noticeId")
	})
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