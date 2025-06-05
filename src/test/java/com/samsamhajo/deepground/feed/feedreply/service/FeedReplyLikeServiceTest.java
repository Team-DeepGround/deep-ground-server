package com.samsamhajo.deepground.feed.feedreply.service;

import com.samsamhajo.deepground.feed.feedreply.entity.FeedReply;
import com.samsamhajo.deepground.feed.feedreply.entity.FeedReplyLike;
import com.samsamhajo.deepground.feed.feedreply.exception.FeedReplyErrorCode;
import com.samsamhajo.deepground.feed.feedreply.exception.FeedReplyException;
import com.samsamhajo.deepground.feed.feedreply.repository.FeedReplyLikeRepository;
import com.samsamhajo.deepground.feed.feedreply.repository.FeedReplyRepository;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.exception.MemberErrorCode;
import com.samsamhajo.deepground.member.exception.MemberException;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FeedReplyLikeServiceTest {

    @Mock
    private FeedReplyRepository feedReplyRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private FeedReplyLikeRepository feedReplyLikeRepository;

    @InjectMocks
    private FeedReplyLikeService feedReplyLikeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCases {
        @Test
        @DisplayName("답글 좋아요 성공")
        void feedReplyLikeIncrease_success() {
            Long feedReplyId = 1L;
            Long memberId = 2L;
            FeedReply feedReply = mock(FeedReply.class);
            Member member = mock(Member.class);

            when(feedReplyLikeRepository.existsByFeedReplyIdAndMemberId(feedReplyId, memberId)).thenReturn(false);
            when(feedReplyRepository.getById(feedReplyId)).thenReturn(feedReply);
            when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
            when(feedReplyLikeRepository.save(any(FeedReplyLike.class))).thenAnswer(invocation -> invocation.getArgument(0));

            feedReplyLikeService.feedReplyLikeIncrease(feedReplyId, memberId);

            verify(feedReplyLikeRepository, times(1)).save(any(FeedReplyLike.class));
        }

        @Test
        @DisplayName("답글 좋아요 취소 성공")
        void feedReplyLikeDecrease_success() {
            Long feedReplyId = 1L;
            Long memberId = 2L;
            FeedReplyLike feedReplyLike = mock(FeedReplyLike.class);

            when(feedReplyLikeRepository.countByFeedReplyId(feedReplyId)).thenReturn(1);
            when(feedReplyLikeRepository.getByFeedReplyIdAndMemberId(feedReplyId, memberId)).thenReturn(feedReplyLike);

            feedReplyLikeService.feedReplyLikeDecrease(feedReplyId, memberId);

            verify(feedReplyLikeRepository, times(1)).delete(feedReplyLike);
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class FailureCases {
        @Test
        @DisplayName("이미 좋아요를 누른 답글에 또 좋아요를 누를 때 예외")
        void feedReplyLikeIncrease_fail_alreadyExists() {
            Long feedReplyId = 1L;
            Long memberId = 2L;

            when(feedReplyLikeRepository.existsByFeedReplyIdAndMemberId(feedReplyId, memberId)).thenReturn(true);

            assertThatThrownBy(() -> feedReplyLikeService.feedReplyLikeIncrease(feedReplyId, memberId))
                    .isInstanceOf(FeedReplyException.class)
                    .hasMessage(FeedReplyErrorCode.FEED_REPLY_LIKE_ALREADY_EXISTS.getMessage());
        }

        @Test
        @DisplayName("멤버가 존재하지 않을 때 예외")
        void feedReplyLikeIncrease_fail_memberNotFound() {
            Long feedReplyId = 1L;
            Long memberId = 2L;

            when(feedReplyLikeRepository.existsByFeedReplyIdAndMemberId(feedReplyId, memberId)).thenReturn(false);
            when(feedReplyRepository.getById(feedReplyId)).thenReturn(mock(FeedReply.class));
            when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> feedReplyLikeService.feedReplyLikeIncrease(feedReplyId, memberId))
                    .isInstanceOf(MemberException.class)
                    .hasMessage(MemberErrorCode.INVALID_MEMBER_ID.getMessage());
        }

        @Test
        @DisplayName("좋아요가 0 이하일 때 취소 시도 예외")
        void feedReplyLikeDecrease_fail_likeMinusNotAllowed() {
            Long feedReplyId = 1L;
            Long memberId = 2L;

            when(feedReplyLikeRepository.countByFeedReplyId(feedReplyId)).thenReturn(0);

            assertThatThrownBy(() -> feedReplyLikeService.feedReplyLikeDecrease(feedReplyId, memberId))
                    .isInstanceOf(FeedReplyException.class)
                    .hasMessage(FeedReplyErrorCode.FEED_LIKE_MINUS_NOT_ALLOWED.getMessage());
        }

        @Test
        @DisplayName("좋아요 취소 시 해당 좋아요가 없을 때 예외")
        void feedReplyLikeDecrease_fail_likeNotFound() {
            Long feedReplyId = 1L;
            Long memberId = 2L;

            when(feedReplyLikeRepository.countByFeedReplyId(feedReplyId)).thenReturn(1);
            when(feedReplyLikeRepository.getByFeedReplyIdAndMemberId(feedReplyId, memberId))
                    .thenThrow(new FeedReplyException(FeedReplyErrorCode.FEED_REPLY_LIKE_NOT_FOUND));

            assertThatThrownBy(() -> feedReplyLikeService.feedReplyLikeDecrease(feedReplyId, memberId))
                    .isInstanceOf(FeedReplyException.class)
                    .hasMessage(FeedReplyErrorCode.FEED_REPLY_LIKE_NOT_FOUND.getMessage());
        }
    }
} 