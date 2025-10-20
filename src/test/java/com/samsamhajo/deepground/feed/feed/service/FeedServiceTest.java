package com.samsamhajo.deepground.feed.feed.service;

import com.samsamhajo.deepground.feed.feed.entity.Feed;
import com.samsamhajo.deepground.feed.feed.exception.FeedErrorCode;
import com.samsamhajo.deepground.feed.feed.exception.FeedException;
import com.samsamhajo.deepground.feed.feed.model.FeedCreateRequest;
import com.samsamhajo.deepground.feed.feed.model.FeedUpdateRequest;
import com.samsamhajo.deepground.feed.feed.model.FetchFeedsResponse;
import com.samsamhajo.deepground.feed.feed.repository.FeedRepository;
import com.samsamhajo.deepground.feed.feedcomment.service.FeedCommentService;
import com.samsamhajo.deepground.feed.feedshared.model.FetchSharedFeedResponse;
import com.samsamhajo.deepground.feed.feedshared.service.SharedFeedService;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.entity.MemberProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FeedServiceTest {

    private FeedRepository feedRepository;
    private FeedMediaService feedMediaService;
    private FeedCommentService feedCommentService;
    private FeedLikeService feedLikeService;
    private SharedFeedService sharedFeedService;
    private FeedService feedService;

    private static final String TEST_CONTENT = "테스트 피드 내용입니다.";
    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "password123";
    private static final String TEST_NICKNAME = "테스트유저";

    @BeforeEach
    void setUp() {
        feedRepository = mock(FeedRepository.class);
        feedMediaService = mock(FeedMediaService.class);
        feedCommentService = mock(FeedCommentService.class);
        feedLikeService = mock(FeedLikeService.class);
        sharedFeedService = mock(SharedFeedService.class);
        feedService = new FeedService(
                feedRepository,
                feedMediaService,
                feedCommentService,
                feedLikeService, sharedFeedService
        );
    }

    @Test
    @DisplayName("피드 생성 성공")
    void createFeedSuccess() {
        // given
        Member testMember = Member.createLocalMember(TEST_EMAIL, TEST_PASSWORD, TEST_NICKNAME);
        FeedCreateRequest request = new FeedCreateRequest(TEST_CONTENT, List.of());
        Feed expectedFeed = Feed.of(TEST_CONTENT, testMember);

        when(feedRepository.save(any(Feed.class))).thenReturn(expectedFeed);

        // when
        Feed createdFeed = feedService.createFeed(request, testMember);

        // then
        assertThat(createdFeed).isNotNull();
        assertThat(createdFeed.getContent()).isEqualTo(TEST_CONTENT);
        assertThat(createdFeed.getMember().getId()).isEqualTo(testMember.getId());

        verify(feedMediaService).createFeedMedia(any(Feed.class), anyList());
    }

    @Test
    @DisplayName("피드 생성 실패 - 내용이 비어있는 경우")
    void createFeedFailWithEmptyContent() {
        // given
        FeedCreateRequest request = new FeedCreateRequest("", List.of());
        Member testMember = Member.createLocalMember(TEST_EMAIL, TEST_PASSWORD, TEST_NICKNAME);

        // when & then
        assertThatThrownBy(() -> feedService.createFeed(request, testMember))
                .isInstanceOf(FeedException.class)
                .hasFieldOrPropertyWithValue("errorCode", FeedErrorCode.INVALID_FEED_CONTENT);
    }

    @Test
    @DisplayName("피드 수정 성공")
    void updateFeedSuccess() {
        // given
        Member testMember = Member.createLocalMember(TEST_EMAIL, TEST_PASSWORD, TEST_NICKNAME);
        Feed existingFeed = Feed.of(TEST_CONTENT, testMember);
        String updatedContent = "수정된 피드 내용입니다.";
        FeedUpdateRequest updateRequest = new FeedUpdateRequest(updatedContent, List.of());
        when(feedRepository.getById(existingFeed.getId())).thenReturn(existingFeed);

        // when
        Feed updatedFeed = feedService.updateFeed(existingFeed.getId(), updateRequest, testMember.getId());

        // then
        assertThat(updatedFeed.getContent()).isEqualTo(updatedContent);
        verify(feedMediaService).updateFeedMedia(any(Feed.class), any(FeedUpdateRequest.class));
    }

    @Test
    @DisplayName("피드 수정 실패 - 내용이 비어있는 경우")
    void updateFeedFailWithEmptyContent() {
        // given
        Member testMember = Member.createLocalMember(TEST_EMAIL, TEST_PASSWORD, TEST_NICKNAME);
        Feed existingFeed = Feed.of(TEST_CONTENT, testMember);
        FeedUpdateRequest updateRequest = new FeedUpdateRequest("", List.of());

        when(feedRepository.findById(existingFeed.getId())).thenReturn(Optional.of(existingFeed));

        // when & then
        assertThatThrownBy(() -> feedService.updateFeed(existingFeed.getId(), updateRequest, testMember.getId()))
                .isInstanceOf(FeedException.class)
                .hasFieldOrPropertyWithValue("errorCode", FeedErrorCode.INVALID_FEED_CONTENT);
    }

    @Test
    @DisplayName("피드 목록 조회 성공")
    void getFeedsSuccess() {
        // given
        Member testMember = Member.createLocalMember(TEST_EMAIL, TEST_PASSWORD, TEST_NICKNAME);

        // 🔸 프로필 생성 & 멤버에 연결 (liveIn은 not-null)
        MemberProfile profile = MemberProfile.create(
                null,              // profileImage
                testMember,
                "소개",            // introduction
                "직업",            // job
                "회사",            // company
                "서울",            // liveIn (NOT NULL)
                "학력",            // education
                new ArrayList<>(), // tech stacks
                null, null, null, null // urls
        );
        // 필요 시 profileId 세팅
        ReflectionTestUtils.setField(profile, "profileId", 10L);

        Feed feed1 = Feed.of("피드1", testMember);
        Feed feed2 = Feed.of("피드2", testMember);

        ReflectionTestUtils.setField(testMember, "id", 1L);
        ReflectionTestUtils.setField(feed1, "id", 1L);
        ReflectionTestUtils.setField(feed2, "id", 2L);
        ReflectionTestUtils.setField(feed1, "createdAt", java.time.LocalDateTime.now());
        ReflectionTestUtils.setField(feed2, "createdAt", java.time.LocalDateTime.now());

        Page<Feed> feedPage = new PageImpl<>(List.of(feed2, feed1));

        when(feedRepository.findAll(any(Pageable.class))).thenReturn(feedPage);
        when(feedMediaService.findAllMediaUrlsByFeedId(anyLong())).thenReturn(List.of());
        when(feedCommentService.countFeedCommentsByFeedId(anyLong())).thenReturn(0);
        when(feedLikeService.countFeedLikeByFeedId(anyLong())).thenReturn(0);
        when(feedLikeService.isLiked(anyLong(), anyLong())).thenReturn(false);
        when(sharedFeedService.countSharedFeedByOriginFeedId(anyLong())).thenReturn(0);

        // when
        FetchFeedsResponse result = feedService.getFeeds(PageRequest.of(0, 10), testMember.getId());

        // then
        assertThat(result.getFeeds()).hasSize(2);
        assertThat(result.getFeeds().get(0).getContent()).isEqualTo("피드2");
        assertThat(result.getFeeds().get(1).getContent()).isEqualTo("피드1");
        assertThat(result.getFeeds().get(0).getMemberId()).isEqualTo(1L);
        assertThat(result.getFeeds().get(1).getMemberId()).isEqualTo(1L);
        assertThat(result.getFeeds().get(0).getMemberName()).isEqualTo(TEST_NICKNAME);
        assertThat(result.getFeeds().get(1).getMemberName()).isEqualTo(TEST_NICKNAME);

        // ✅ profileId 검증
        assertThat(result.getFeeds().get(0).getProfileId()).isEqualTo(10L);
        assertThat(result.getFeeds().get(1).getProfileId()).isEqualTo(10L);

        assertThat(result.getFeeds().get(0).getShareCount()).isEqualTo(0);
        assertThat(result.getFeeds().get(1).getShareCount()).isEqualTo(0);
        assertThat(result.getFeeds().get(0).isShared()).isFalse();
        assertThat(result.getFeeds().get(1).isShared()).isFalse();
        assertThat(result.getFeeds().get(0).getSharedFeed()).isNull();
        assertThat(result.getFeeds().get(1).getSharedFeed()).isNull();
    }


    @Test
    @DisplayName("피드 목록 조회 성공")
    void getFeedsSuccessWithSharedFeed() {
        // given
        Member testMember = Member.createLocalMember(TEST_EMAIL, TEST_PASSWORD, TEST_NICKNAME);

        // 🔸 프로필 생성 & 멤버에 연결
        MemberProfile profile = MemberProfile.create(
                null, testMember, "소개", "직업", "회사", "서울", "학력",
                new ArrayList<>(), null, null, null, null
        );
        ReflectionTestUtils.setField(profile, "profileId", 10L);

        Feed feed1 = Feed.of("피드1", testMember);
        Feed feed2 = Feed.of("피드2", testMember);

        ReflectionTestUtils.setField(testMember, "id", 1L);
        ReflectionTestUtils.setField(feed1, "id", 1L);
        ReflectionTestUtils.setField(feed2, "id", 2L);
        ReflectionTestUtils.setField(feed1, "createdAt", java.time.LocalDateTime.now());
        ReflectionTestUtils.setField(feed2, "createdAt", java.time.LocalDateTime.now());

        Page<Feed> feedPage = new PageImpl<>(List.of(feed2, feed1));

        when(feedRepository.findAll(any(Pageable.class))).thenReturn(feedPage);
        when(feedMediaService.findAllMediaUrlsByFeedId(anyLong())).thenReturn(List.of());
        when(feedCommentService.countFeedCommentsByFeedId(anyLong())).thenReturn(0);
        when(feedLikeService.countFeedLikeByFeedId(anyLong())).thenReturn(0);
        when(feedLikeService.isLiked(anyLong(), anyLong())).thenReturn(false);
        when(sharedFeedService.countSharedFeedByOriginFeedId(1L)).thenReturn(5);
        when(sharedFeedService.countSharedFeedByOriginFeedId(2L)).thenReturn(0);

        // when
        FetchFeedsResponse result = feedService.getFeeds(PageRequest.of(0, 10), testMember.getId());

        // then
        assertThat(result.getFeeds()).hasSize(2);
        assertThat(result.getFeeds().get(0).getContent()).isEqualTo("피드2");
        assertThat(result.getFeeds().get(1).getContent()).isEqualTo("피드1");

        // ✅ profileId 검증
        assertThat(result.getFeeds().get(0).getProfileId()).isEqualTo(10L);
        assertThat(result.getFeeds().get(1).getProfileId()).isEqualTo(10L);
        assertThat(result.getFeeds().get(0).getShareCount()).isEqualTo(0);
        assertThat(result.getFeeds().get(1).getShareCount()).isEqualTo(5);
        assertThat(result.getFeeds().get(0).getSharedFeed()).isNull();
    }


    @Test
    @DisplayName("피드 삭제 성공")
    void deleteFeedSuccess() {
        // given
        Member testMember = Member.createLocalMember(TEST_EMAIL, TEST_PASSWORD, TEST_NICKNAME);
        Feed existingFeed = Feed.of(TEST_CONTENT, testMember);

        when(feedRepository.getById(existingFeed.getId())).thenReturn(existingFeed);

        // when
        feedService.deleteFeed(existingFeed.getId());

        // then
        verify(feedCommentService).deleteFeedCommentByFeed(existingFeed.getId());
        verify(feedLikeService).deleteAllByFeedId(existingFeed.getId());
        verify(feedMediaService).deleteAllByFeedId(existingFeed.getId());
        verify(feedRepository).deleteById(existingFeed.getId());
    }
}
