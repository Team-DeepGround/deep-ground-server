package com.samsamhajo.deepground.feed.feed.service;

import com.samsamhajo.deepground.feed.feed.entity.Feed;
import com.samsamhajo.deepground.feed.feed.entity.FeedLike;
import com.samsamhajo.deepground.feed.feed.exception.FeedErrorCode;
import com.samsamhajo.deepground.feed.feed.exception.FeedException;
import com.samsamhajo.deepground.feed.feed.repository.FeedLikeRepository;
import com.samsamhajo.deepground.feed.feed.repository.FeedRepository;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.exception.MemberErrorCode;
import com.samsamhajo.deepground.member.exception.MemberException;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeedLikeServiceTest {

    @InjectMocks
    private FeedLikeService feedLikeService;

    @Mock
    private FeedRepository feedRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private FeedLikeRepository feedLikeRepository;

    private final Member member = Member.createLocalMember("test@naver.com", "password", "testUser");
    private final Feed feed = Feed.of("테스트 피드 내용", member);

    @Test
    @DisplayName("피드 좋아요 증가 성공 테스트")
    void feedLikeIncrease_Success() {
        // given
        Long feedId = 1L;
        Long memberId = 1L;

        given(feedRepository.getById(feedId)).willReturn(feed);
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
        given(feedLikeRepository.existsByFeedIdAndMemberId(feedId, memberId)).willReturn(false);

        // when
        feedLikeService.feedLikeIncrease(feedId, memberId);

        // then
        verify(feedLikeRepository, times(1)).save(any(FeedLike.class));
    }

    @Test
    @DisplayName("피드 좋아요 증가 실패 테스트 - 이미 좋아요한 경우")
    void feedLikeIncrease_Fail_AlreadyLiked() {
        // given
        Long feedId = 1L;
        Long memberId = 1L;

        given(feedLikeRepository.existsByFeedIdAndMemberId(feedId, memberId)).willReturn(true);

        // when & then
        assertThatThrownBy(() -> feedLikeService.feedLikeIncrease(feedId, memberId))
                .isInstanceOf(FeedException.class)
                .hasFieldOrPropertyWithValue("errorCode", FeedErrorCode.FEED_LIKE_ALREADY_EXISTS);
    }

    @Test
    @DisplayName("피드 좋아요 증가 실패 테스트 - 존재하지 않는 회원")
    void feedLikeIncrease_Fail_MemberNotFound() {
        // given
        Long feedId = 1L;
        Long nonExistentMemberId = 999L;

        given(feedLikeRepository.existsByFeedIdAndMemberId(feedId, nonExistentMemberId)).willReturn(false);
        given(memberRepository.findById(nonExistentMemberId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> feedLikeService.feedLikeIncrease(feedId, nonExistentMemberId))
                .isInstanceOf(MemberException.class)
                .hasFieldOrPropertyWithValue("errorCode", MemberErrorCode.INVALID_MEMBER_ID);
    }

    @Test
    @DisplayName("피드 좋아요 감소 성공 테스트")
    void feedLikeDecrease_Success() {
        // given
        Long feedId = 1L;
        Long memberId = 1L;
        FeedLike feedLike = FeedLike.of(feed, member);

        given(feedLikeRepository.countByFeedId(feedId)).willReturn(1);
        given(feedLikeRepository.getByFeedIdAndMemberId(feedId, memberId)).willReturn(feedLike);

        // when
        feedLikeService.feedLikeDecrease(feedId, memberId);

        // then
        verify(feedLikeRepository, times(1)).delete(feedLike);
    }

    @Test
    @DisplayName("피드 좋아요 감소 실패 테스트 - 좋아요가 없는 경우")
    void feedLikeDecrease_Fail_NoLikes() {
        // given
        Long feedId = 1L;
        Long memberId = 1L;

        given(feedLikeRepository.countByFeedId(feedId)).willReturn(0);

        // when & then
        assertThatThrownBy(() -> feedLikeService.feedLikeDecrease(feedId, memberId))
                .isInstanceOf(FeedException.class)
                .hasFieldOrPropertyWithValue("errorCode", FeedErrorCode.FEED_LIKE_MINUS_NOT_ALLOWED);
    }

    @Test
    @DisplayName("피드 좋아요 수 조회 테스트")
    void countFeedLikeByFeedId_Success() {
        // given
        Long feedId = 1L;
        int expectedCount = 5;

        given(feedLikeRepository.countByFeedId(feedId)).willReturn(expectedCount);

        // when
        int actualCount = feedLikeService.countFeedLikeByFeedId(feedId);

        // then
        assertThat(actualCount).isEqualTo(expectedCount);
    }
} 