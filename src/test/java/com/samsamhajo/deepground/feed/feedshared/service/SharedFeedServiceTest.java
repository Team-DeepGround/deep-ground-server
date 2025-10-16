package com.samsamhajo.deepground.feed.feedshared.service;

import com.samsamhajo.deepground.feed.feed.entity.Feed;
import com.samsamhajo.deepground.feed.feed.repository.FeedRepository;
import com.samsamhajo.deepground.feed.feed.service.FeedMediaService;
import com.samsamhajo.deepground.feed.feedshared.dto.SharedFeedRequest;
import com.samsamhajo.deepground.feed.feedshared.entity.SharedFeed;
import com.samsamhajo.deepground.feed.feedshared.exception.SharedFeedErrorCode;
import com.samsamhajo.deepground.feed.feedshared.exception.SharedFeedException;
import com.samsamhajo.deepground.feed.feedshared.model.FetchSharedFeedResponse;
import com.samsamhajo.deepground.feed.feedshared.repository.SharedFeedRepository;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.entity.MemberProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SharedFeedServiceTest {

    private SharedFeedRepository sharedFeedRepository;
    private FeedRepository feedRepository;
    private FeedMediaService feedMediaService;
    private SharedFeedService sharedFeedService;

    private static final String TEST_CONTENT = "테스트 피드 내용입니다.";
    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "password123";
    private static final String TEST_NICKNAME = "테스트유저";

    @BeforeEach
    void setUp() {
        sharedFeedRepository = mock(SharedFeedRepository.class);
        feedRepository = mock(FeedRepository.class);
        feedMediaService = mock(FeedMediaService.class);
        sharedFeedService = new SharedFeedService(
                sharedFeedRepository,
                feedRepository,
                feedMediaService
        );
    }

    @Test
    @DisplayName("공유 피드 생성 성공")
    void createSharedFeedSuccess() {
        // given
        Member testMember = Member.createLocalMember(TEST_EMAIL, TEST_PASSWORD, TEST_NICKNAME);
        Feed originFeed = Feed.of(TEST_CONTENT, testMember);
        Feed newFeed = Feed.of(TEST_CONTENT, testMember);
        SharedFeed sharedFeed = SharedFeed.of(newFeed, originFeed, testMember);

        ReflectionTestUtils.setField(testMember, "id", 1L);
        ReflectionTestUtils.setField(originFeed, "id", 1L);
        ReflectionTestUtils.setField(newFeed, "id", 2L);

        SharedFeedRequest request = new SharedFeedRequest();
        ReflectionTestUtils.setField(request, "originFeedId", 1L);
        ReflectionTestUtils.setField(request, "content", TEST_CONTENT);

        when(feedRepository.getById(1L)).thenReturn(originFeed);
        when(feedRepository.save(any(Feed.class))).thenReturn(newFeed);
        when(sharedFeedRepository.save(any(SharedFeed.class))).thenReturn(sharedFeed);

        // when
        SharedFeed result = sharedFeedService.createSharedFeed(request, testMember);

        // then
        assertThat(result).isNotNull();
        verify(feedRepository).getById(1L);
        verify(feedRepository).save(any(Feed.class));
        verify(sharedFeedRepository).save(any(SharedFeed.class));
    }

    @Test
    @DisplayName("공유 피드 조회 성공")
    void getSharedFeedResponseSuccess() {
        // given
        Member testMember = Member.createLocalMember(TEST_EMAIL, TEST_PASSWORD, TEST_NICKNAME);

        // 🔸 원본 작성자 프로필 생성 & 연결
        MemberProfile profile = MemberProfile.create(
                null, testMember, "소개", "직업", "회사", "서울", "학력",
                new ArrayList<>(), null, null, null, null
        );
        ReflectionTestUtils.setField(profile, "profileId", 20L);

        Feed originFeed = Feed.of(TEST_CONTENT, testMember);
        Feed newFeed = Feed.of(TEST_CONTENT, testMember);
        SharedFeed sharedFeed = SharedFeed.of(newFeed, originFeed, testMember);

        ReflectionTestUtils.setField(testMember, "id", 1L);
        ReflectionTestUtils.setField(originFeed, "id", 1L);
        ReflectionTestUtils.setField(newFeed, "id", 2L);
        ReflectionTestUtils.setField(sharedFeed, "id", 1L);
        ReflectionTestUtils.setField(originFeed, "createdAt", LocalDateTime.now());
        ReflectionTestUtils.setField(newFeed, "createdAt", LocalDateTime.now());

        when(sharedFeedRepository.getOrNullByFeedId(2L)).thenReturn(sharedFeed);
        when(feedMediaService.findAllMediaIdsByFeedId(1L)).thenReturn(List.of(1L, 2L));

        // when
        FetchSharedFeedResponse response = sharedFeedService.getSharedFeedResponse(2L);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getFeedId()).isEqualTo(1L);
        assertThat(response.getMemberId()).isEqualTo(1L);
        assertThat(response.getMemberName()).isEqualTo(TEST_NICKNAME);
        assertThat(response.getContent()).isEqualTo(TEST_CONTENT);
        assertThat(response.getMediaIds()).hasSize(2);

        // (선택) FetchSharedFeedResponse 모델에 profileId를 추가했다면 검증
        // assertThat(response.getProfileId()).isEqualTo(20L);
    }


    @Test
    @DisplayName("공유 피드 조회 실패 - 존재하지 않는 공유 피드")
    void getSharedFeedResponseFailWithNotFound() {
        // given
        when(sharedFeedRepository.getOrNullByFeedId(1L)).thenReturn(null);

        // when
        FetchSharedFeedResponse response = sharedFeedService.getSharedFeedResponse(1L);

        // then
        assertThat(response).isNull();
    }

    @Test
    @DisplayName("원본 피드별 공유 수 조회 성공")
    void countSharedFeedByOriginFeedIdSuccess() {
        // given
        when(sharedFeedRepository.countAllByOriginFeedId(1L)).thenReturn(5);

        // when
        int count = sharedFeedService.countSharedFeedByOriginFeedId(1L);

        // then
        assertThat(count).isEqualTo(5);
    }

    @Test
    @DisplayName("피드 ID로 공유 피드 조회 성공")
    void findByFeedIdSuccess() {
        // given
        Member testMember = Member.createLocalMember(TEST_EMAIL, TEST_PASSWORD, TEST_NICKNAME);
        Feed originFeed = Feed.of(TEST_CONTENT, testMember);
        Feed newFeed = Feed.of(TEST_CONTENT, testMember);
        SharedFeed sharedFeed = SharedFeed.of(newFeed, originFeed, testMember);

        ReflectionTestUtils.setField(testMember, "id", 1L);
        ReflectionTestUtils.setField(originFeed, "id", 1L);
        ReflectionTestUtils.setField(newFeed, "id", 2L);
        ReflectionTestUtils.setField(sharedFeed, "id", 1L);

        when(sharedFeedRepository.getByFeedId(2L)).thenReturn(sharedFeed);

        // when
        SharedFeed foundSharedFeed = sharedFeedService.findByFeedId(2L);

        // then
        assertThat(foundSharedFeed).isNotNull();
        assertThat(foundSharedFeed.getId()).isEqualTo(1L);
        assertThat(foundSharedFeed.getFeed().getId()).isEqualTo(2L);
        assertThat(foundSharedFeed.getOriginFeed().getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("피드 ID로 공유 피드 조회 실패 - 존재하지 않는 공유 피드")
    void findByFeedIdFailWithNotFound() {
        // given
        when(sharedFeedRepository.getByFeedId(1L))
                .thenThrow(new SharedFeedException(SharedFeedErrorCode.SHARED_FEED_NOT_FOUND));

        // when & then
        assertThatThrownBy(() -> sharedFeedService.findByFeedId(1L))
                .isInstanceOf(SharedFeedException.class)
                .hasFieldOrPropertyWithValue("errorCode", SharedFeedErrorCode.SHARED_FEED_NOT_FOUND);
    }
} 
