package com.samsamhajo.deepground.feed.feedcomment.service;

import com.samsamhajo.deepground.feed.feed.entity.Feed;
import com.samsamhajo.deepground.feed.feedcomment.entity.FeedComment;
import com.samsamhajo.deepground.feed.feedcomment.entity.FeedCommentLike;
import com.samsamhajo.deepground.feed.feedcomment.exception.FeedCommentErrorCode;
import com.samsamhajo.deepground.feed.feedcomment.exception.FeedCommentException;
import com.samsamhajo.deepground.feed.feedcomment.repository.FeedCommentLikeRepository;
import com.samsamhajo.deepground.feed.feedcomment.repository.FeedCommentRepository;
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
class FeedCommentLikeServiceTest {

    @InjectMocks
    private FeedCommentLikeService feedCommentLikeService;

    @Mock
    private FeedCommentRepository feedCommentRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private FeedCommentLikeRepository feedCommentLikeRepository;

    private final Member member = Member.createLocalMember("test@naver.com", "password", "testUser");
    private final Feed feed = Feed.of("테스트 피드 내용", member);
    private final FeedComment feedComment = FeedComment.of("테스트 댓글 내용", feed, member);

    @Test
    @DisplayName("피드 댓글 좋아요 증가 성공 테스트")
    void feedLikeIncrease_Success() {
        // given
        Long feedCommentId = 1L;
        Long memberId = 1L;
        FeedComment feedComment = mock(FeedComment.class);
        Member member = mock(Member.class);

        given(feedCommentRepository.getById(feedCommentId)).willReturn(feedComment);
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
        given(feedCommentLikeRepository.existsByFeedCommentIdAndMemberId(feedCommentId, memberId)).willReturn(false);

        // when
        feedCommentLikeService.feedLikeIncrease(feedCommentId, memberId);

        // then
        verify(feedCommentLikeRepository, times(1)).save(any(FeedCommentLike.class));
    }

    @Test
    @DisplayName("피드 댓글 좋아요 증가 실패 테스트 - 이미 좋아요한 경우")
    void feedLikeIncrease_Fail_AlreadyLiked() {
        // given
        Long feedCommentId = 1L;
        Long memberId = 1L;

        given(feedCommentLikeRepository.existsByFeedCommentIdAndMemberId(feedCommentId, memberId)).willReturn(true);

        // when & then
        assertThatThrownBy(() -> feedCommentLikeService.feedLikeIncrease(feedCommentId, memberId))
                .isInstanceOf(FeedCommentException.class)
                .hasFieldOrPropertyWithValue("errorCode", FeedCommentErrorCode.FEED_COMMENT_LIKE_ALREADY_EXISTS);
    }

    @Test
    @DisplayName("피드 댓글 좋아요 증가 실패 테스트 - 존재하지 않는 회원")
    void feedLikeIncrease_Fail_MemberNotFound() {
        // given
        Long feedCommentId = 1L;
        Long nonExistentMemberId = 999L;

        given(feedCommentLikeRepository.existsByFeedCommentIdAndMemberId(feedCommentId, nonExistentMemberId)).willReturn(false);
        given(memberRepository.findById(nonExistentMemberId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> feedCommentLikeService.feedLikeIncrease(feedCommentId, nonExistentMemberId))
                .isInstanceOf(MemberException.class)
                .hasFieldOrPropertyWithValue("errorCode", MemberErrorCode.INVALID_MEMBER_ID);
    }

    @Test
    @DisplayName("피드 댓글 좋아요 감소 성공 테스트")
    void feedLikeDecrease_Success() {
        // given
        Long feedCommentId = 1L;
        Long memberId = 1L;
        FeedCommentLike feedCommentLike = mock(FeedCommentLike.class);

        given(feedCommentLikeRepository.countByFeedCommentId(feedCommentId)).willReturn(1);
        given(feedCommentLikeRepository.getByFeedCommentIdAndMemberId(feedCommentId, memberId)).willReturn(feedCommentLike);

        // when
        feedCommentLikeService.feedLikeDecrease(feedCommentId, memberId);

        // then
        verify(feedCommentLikeRepository, times(1)).delete(feedCommentLike);
    }

    @Test
    @DisplayName("피드 댓글 좋아요 감소 실패 테스트 - 좋아요가 없는 경우")
    void feedLikeDecrease_Fail_NoLikes() {
        // given
        Long feedCommentId = 1L;
        Long memberId = 1L;

        given(feedCommentLikeRepository.countByFeedCommentId(feedCommentId)).willReturn(0);

        // when & then
        assertThatThrownBy(() -> feedCommentLikeService.feedLikeDecrease(feedCommentId, memberId))
                .isInstanceOf(FeedCommentException.class)
                .hasFieldOrPropertyWithValue("errorCode", FeedCommentErrorCode.FEED_LIKE_MINUS_NOT_ALLOWED);
    }

    @Test
    @DisplayName("피드 댓글 좋아요 수 조회 테스트")
    void countFeedCommentLikeByFeedId_Success() {
        // given
        Long feedCommentId = 1L;
        int expectedCount = 5;

        given(feedCommentLikeRepository.countByFeedCommentId(feedCommentId)).willReturn(expectedCount);

        // when
        int actualCount = feedCommentLikeService.countFeedCommentLikeByFeedId(feedCommentId);

        // then
        assertThat(actualCount).isEqualTo(expectedCount);
    }
} 