package com.coffeekiosk.coffeekiosk.service.notice;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.coffeekiosk.coffeekiosk.IntegrationTestSupport;
import com.coffeekiosk.coffeekiosk.domain.notice.Notice;
import com.coffeekiosk.coffeekiosk.domain.notice.NoticeRepository;
import com.coffeekiosk.coffeekiosk.domain.user.Role;
import com.coffeekiosk.coffeekiosk.domain.user.User;
import com.coffeekiosk.coffeekiosk.domain.user.UserRepository;
import com.coffeekiosk.coffeekiosk.service.notice.request.NoticeSaveUpdateServiceRequest;
import com.coffeekiosk.coffeekiosk.service.notice.response.NoticeResponse;

class NoticeServiceTest extends IntegrationTestSupport {

	@Autowired
	private NoticeService noticeService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private NoticeRepository noticeRepository;

	@AfterEach
	void tearDown() {
		noticeRepository.deleteAllInBatch();
		userRepository.deleteAllInBatch();
	}

	@DisplayName("공지사항을 작성한다.")
	@Test
	void createNotice() {
		//given
		User user = createUser();
		userRepository.save(user);

		LocalDateTime registeredDateTime = LocalDateTime.of(2023, 11, 21, 0, 0);

		NoticeSaveUpdateServiceRequest request = NoticeSaveUpdateServiceRequest.builder()
			.title("제목1")
			.content("내용1")
			.build();

		//when
		NoticeResponse response = noticeService.createNotice(user.getId(), request, registeredDateTime);

		//then
		assertThat(response.getId()).isNotNull();
		assertThat(response)
			.extracting("title", "content", "registeredDateTime", "userId", "userName")
			.contains(
				"제목1", "내용1", registeredDateTime, user.getId(), user.getName()
			);
	}

	@DisplayName("공지사항 내용을 수정한다.")
	@Test
	void updateNotice() {
		//given
		User user = createUser();
		userRepository.save(user);

		LocalDateTime registeredDateTime = LocalDateTime.of(2023, 11, 21, 0, 0);
		Notice notice = createNotice("제목1", "내용1", user, registeredDateTime);
		noticeRepository.save(notice);

		NoticeSaveUpdateServiceRequest request = NoticeSaveUpdateServiceRequest.builder()
			.title("제목_수정")
			.content("내용_수정")
			.build();

		//when
		NoticeResponse response = noticeService.updateNotice(notice.getId(), request);

		//then
		assertThat(response)
			.extracting("id", "title", "content", "registeredDateTime", "userId", "userName")
			.contains(
				notice.getId(), "제목_수정", "내용_수정", registeredDateTime, user.getId(), user.getName()
			);
	}

	@DisplayName("공지사항을 조회한다.")
	@Test
	void findNoticeById() {
		//given
		User user = createUser();
		userRepository.save(user);

		LocalDateTime registeredDateTime = LocalDateTime.of(2023, 11, 21, 0, 0);
		Notice notice = createNotice("제목1", "내용1",  user,  registeredDateTime);
		noticeRepository.save(notice);

		//when
		NoticeResponse response = noticeService.findById(notice.getId());

		//then
		assertThat(response)
			.extracting("id", "title", "content", "registeredDateTime", "userId", "userName")
			.contains(
				notice.getId(), "제목1", "내용1", registeredDateTime, user.getId(), user.getName()
			);
	}

	@DisplayName("모든 공지사항 목록을 반환한다.")
	@Test
	void findNotices() {
		//given
		LocalDateTime registeredDateTime = LocalDateTime.of(2023, 11, 21, 0, 0);

		User user = createUser();
		userRepository.save(user);

		Notice notice1 = createNotice("제목1", "내용1", user, registeredDateTime);
		Notice notice2 = createNotice("제목2", "내용2", user, registeredDateTime);
		noticeRepository.saveAll(List.of(notice1, notice2));

		//when
		List<NoticeResponse> notices = noticeService.findNotices();

		//then
		assertThat(notices)
			.extracting("id", "title", "content", "registeredDateTime", "userId", "userName")
			.contains(
				tuple(notice1.getId(), "제목1", "내용1", registeredDateTime, user.getId(), user.getName()),
				tuple(notice2.getId(), "제목2", "내용2", registeredDateTime, user.getId(), user.getName())
			);
	}

	@DisplayName("공지사항을 삭제한다.")
	@Test
	void deleteNotice() {
		//given
		User user = createUser();
		userRepository.save(user);

		LocalDateTime registeredDateTime = LocalDateTime.of(2023, 11, 21, 0, 0);
		Notice notice = createNotice("제목1", "내용1", user, registeredDateTime);
		noticeRepository.save(notice);

		//when
		noticeService.delete(notice.getId());

		//then
		List<Notice> notices = noticeRepository.findAll();
		assertThat(notices).hasSize(0)
			.isEmpty();
	}

	private Notice createNotice(String title, String content, User user, LocalDateTime registeredDateTime) {
		return Notice.builder()
			.user(user)
			.title(title)
			.content(content)
			.registeredDateTime(registeredDateTime)
			.build();
	}

	private User createUser() {
		return User.builder()
			.email("test@coffeekiosk.com")
			.name("우경서")
			.role(Role.USER)
			.build();
	}
}