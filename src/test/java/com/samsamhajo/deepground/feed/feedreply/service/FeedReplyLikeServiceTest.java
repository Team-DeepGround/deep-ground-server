package com.samsamhajo.deepground.feed.feedreply.service;

import com.samsamhajo.deepground.feed.feedcomment.entity.FeedComment;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FeedReplyLikeServiceTest {

    @Mock
    private FeedReplyRepository feedReplyRepository;

    @Mock
    private FeedReplyLikeRepository feedReplyLikeRepository;

    @InjectMocks
    private FeedReplyLikeService feedReplyLikeService;

    @Mock
    private MemberRepository memberRepository;

    private static final String TEST_CONTENT = "테스트 답글 내용입니다.";
    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "password123";
    private static final String TEST_NICKNAME = "테스트유저";

    private Member testMember;
    private FeedComment testFeedComment;
    private FeedReply testFeedReply;
    private FeedReplyLike testFeedReplyLike;

    @BeforeEach
    void setUp() {
        testMember = Member.createLocalMember(TEST_EMAIL, TEST_PASSWORD, TEST_NICKNAME);
        testFeedComment = FeedComment.of(TEST_CONTENT, null, testMember);
        testFeedReply = FeedReply.of(TEST_CONTENT, testFeedComment, testMember);
        testFeedReplyLike = FeedReplyLike.of(testFeedReply, testMember);

        ReflectionTestUtils.setField(testMember, "id", 1L);
        ReflectionTestUtils.setField(testFeedComment, "id", 1L);
        ReflectionTestUtils.setField(testFeedReply, "id", 1L);
        ReflectionTestUtils.setField(testFeedReplyLike, "id", 1L);
    }

    @Test
    @DisplayName("피드 답글 좋아요 증가 성공")
    void feedReplyLikeIncreaseSuccess() {
        // given
        when(feedReplyRepository.getById(1L)).thenReturn(testFeedReply);
        when(feedReplyLikeRepository.existsByFeedReplyIdAndMemberId(1L, 1L)).thenReturn(false);
        when(feedReplyLikeRepository.save(any(FeedReplyLike.class))).thenReturn(testFeedReplyLike);
        when(memberRepository.findById(1L)).thenReturn(Optional.of(testMember));

        // when
        feedReplyLikeService.feedReplyLikeIncrease(1L, 1L);

        // then
        verify(feedReplyLikeRepository).save(any(FeedReplyLike.class));
    }

    @Test
    @DisplayName("피드 답글 좋아요 증가 실패 - 이미 좋아요를 누른 경우")
    void feedReplyLikeIncreaseFailWithAlreadyLiked() {
        // given
        when(feedReplyLikeRepository.existsByFeedReplyIdAndMemberId(1L, 1L)).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> feedReplyLikeService.feedReplyLikeIncrease(1L, 1L))
                .isInstanceOf(FeedReplyException.class)
                .hasMessage(FeedReplyErrorCode.FEED_REPLY_LIKE_ALREADY_EXISTS.getMessage());
    }

    @Test
    @DisplayName("피드 답글 좋아요 증가 실패 - 존재하지 않는 회원")
    void feedReplyLikeIncreaseFailWithInvalidMember() {
        // given
        when(feedReplyLikeRepository.existsByFeedReplyIdAndMemberId(1L, 1L))
                .thenThrow(new MemberException(MemberErrorCode.INVALID_MEMBER_ID));

        // when & then
        assertThatThrownBy(() -> feedReplyLikeService.feedReplyLikeIncrease(1L, 1L))
                .isInstanceOf(MemberException.class)
                .hasFieldOrPropertyWithValue("errorCode", MemberErrorCode.INVALID_MEMBER_ID);
    }

    @Test
    @DisplayName("피드 답글 좋아요 감소 성공")
    void feedReplyLikeDecreaseSuccess() {
        // given
        when(feedReplyLikeRepository.getByFeedReplyIdAndMemberId(1L, 1L))
                .thenReturn(testFeedReplyLike);
        when(feedReplyLikeRepository.countByFeedReplyId(1L)).thenReturn(1);

        // when
        feedReplyLikeService.feedReplyLikeDecrease(1L, 1L);

        // then
        verify(feedReplyLikeRepository).delete(testFeedReplyLike);
    }

    @Test
    @DisplayName("피드 답글 좋아요 감소 실패 - 좋아요가 없는 경우")
    void feedReplyLikeDecreaseFailWithNoLikes() {
        // when & then
        assertThatThrownBy(() -> feedReplyLikeService.feedReplyLikeDecrease(1L, 1L))
                .isInstanceOf(FeedReplyException.class)
                .hasMessage(FeedReplyErrorCode.FEED_LIKE_MINUS_NOT_ALLOWED.getMessage());
    }

    @Test
    @DisplayName("피드 답글 좋아요 수 조회 성공")
    void countFeedReplyLikeByFeedReplyIdSuccess() {
        // given
        when(feedReplyLikeRepository.countByFeedReplyId(1L)).thenReturn(5);

        // when
        int likeCount = feedReplyLikeService.countFeedReplyLikeByFeedReplyId(1L);

        // then
        assertThat(likeCount).isEqualTo(5L);
    }

    @Test
    @DisplayName("피드 답글의 모든 좋아요 삭제 성공")
    void deleteAllByFeedReplyIdSuccess() {
        // given & when
        feedReplyLikeService.deleteAllByFeedReplyId(1L);

        // then
        verify(feedReplyLikeRepository).deleteAllByFeedReplyId(1L);
    }

    @Test
    @DisplayName("피드 답글 좋아요 여부 확인 성공")
    void isLikedSuccess() {
        // given
        when(feedReplyLikeRepository.existsByFeedReplyIdAndMemberId(1L, 1L)).thenReturn(true);

        // when
        boolean isLiked = feedReplyLikeService.isLiked(1L, 1L);

        // then
        assertThat(isLiked).isTrue();
    }
} 