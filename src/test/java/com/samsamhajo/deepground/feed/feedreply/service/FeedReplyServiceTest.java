package com.samsamhajo.deepground.feed.feedreply.service;

import com.samsamhajo.deepground.feed.feedcomment.entity.FeedComment;
import com.samsamhajo.deepground.feed.feedcomment.exception.FeedCommentErrorCode;
import com.samsamhajo.deepground.feed.feedcomment.exception.FeedCommentException;
import com.samsamhajo.deepground.feed.feedcomment.repository.FeedCommentRepository;
import com.samsamhajo.deepground.feed.feedreply.entity.FeedReply;
import com.samsamhajo.deepground.feed.feedreply.exception.FeedReplyErrorCode;
import com.samsamhajo.deepground.feed.feedreply.exception.FeedReplyException;
import com.samsamhajo.deepground.feed.feedreply.model.FeedReplyCreateRequest;
import com.samsamhajo.deepground.feed.feedreply.model.FeedReplyUpdateRequest;
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

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FeedReplyServiceTest {

    @Mock
    private FeedCommentRepository feedCommentRepository;
    @Mock
    private FeedReplyRepository feedReplyRepository;
    @Mock
    private FeedReplyMediaService feedReplyMediaService;
    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private FeedReplyService feedReplyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCases {
        @Test
        @DisplayName("정상적으로 답글 생성")
        void createFeedReply_success() {
            // given
            Long memberId = 1L;
            Long feedCommentId = 10L;
            String content = "테스트 답글";
            FeedReplyCreateRequest request = new FeedReplyCreateRequest(feedCommentId, content, Collections.emptyList());

            Member member = mock(Member.class);
            FeedComment feedComment = mock(FeedComment.class);

            when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
            when(feedCommentRepository.getById(feedCommentId)).thenReturn(feedComment);
            when(feedReplyRepository.save(any(FeedReply.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // when
            FeedComment result = feedReplyService.createFeedReply(request, memberId);

            // then
            assertThat(result).isEqualTo(feedComment);
            verify(feedReplyRepository, times(1)).save(any(FeedReply.class));
            verify(feedReplyMediaService, times(1)).createFeedReplyMedia(any(FeedReply.class), eq(Collections.emptyList()));
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class FailureCases {
        @Test
        @DisplayName("답글 내용이 비어있으면 예외 발생")
        void createFeedReply_fail_emptyContent() {
            // given
            FeedReplyCreateRequest request = new FeedReplyCreateRequest(1L, "", Collections.emptyList());

            // when & then
            assertThatThrownBy(() -> feedReplyService.createFeedReply(request, 1L))
                    .isInstanceOf(FeedCommentException.class)
                    .hasMessage(FeedCommentErrorCode.INVALID_FEED_COMMENT_CONTENT.getMessage());
        }

        @Test
        @DisplayName("멤버가 존재하지 않으면 예외 발생")
        void createFeedReply_fail_memberNotFound() {
            // given
            Long memberId = 1L;
            FeedReplyCreateRequest request = new FeedReplyCreateRequest(1L, "content", Collections.emptyList());

            when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> feedReplyService.createFeedReply(request, memberId))
                    .isInstanceOf(MemberException.class)
                    .hasMessage(MemberErrorCode.INVALID_MEMBER_ID.getMessage());
        }

        @Test
        @DisplayName("피드 댓글이 존재하지 않으면 예외 발생")
        void createFeedReply_fail_feedCommentNotFound() {
            // given
            Long memberId = 1L;
            Long feedCommentId = 10L;
            FeedReplyCreateRequest request = new FeedReplyCreateRequest(feedCommentId, "content", Collections.emptyList());

            Member member = mock(Member.class);
            when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
            when(feedCommentRepository.getById(feedCommentId)).thenThrow(new FeedCommentException(FeedCommentErrorCode.FEED_COMMENT_NOT_FOUND));

            // when & then
            assertThatThrownBy(() -> feedReplyService.createFeedReply(request, memberId))
                    .isInstanceOf(FeedCommentException.class)
                    .hasMessage(FeedCommentErrorCode.FEED_COMMENT_NOT_FOUND.getMessage());
        }
    }

    @Nested
    @DisplayName("답글 수정(업데이트) 케이스")
    class UpdateCases {
        @Test
        @DisplayName("정상적으로 답글 수정")
        void updateFeed_success() {
            // given
            Long feedReplyId = 1L;
            String newContent = "수정된 답글";
            java.util.List images = Collections.emptyList();
            FeedReplyUpdateRequest request = new FeedReplyUpdateRequest(newContent, images);

            FeedReply feedReply = mock(FeedReply.class);
            when(feedReplyRepository.getById(feedReplyId)).thenReturn(feedReply);

            // when
            FeedReply result = feedReplyService.updateFeedReply(feedReplyId, request);

            // then
            verify(feedReply).updateContent(newContent);
            verify(feedReplyMediaService, times(1)).updateFeedReplyMedia(feedReply, images);
            assertThat(result).isEqualTo(feedReply);
        }

        @Test
        @DisplayName("답글 내용이 비어있으면 예외 발생")
        void updateFeed_fail_emptyContent() {
            // given
            Long feedReplyId = 1L;
            FeedReplyUpdateRequest request = new FeedReplyUpdateRequest("", Collections.emptyList());

            // when & then
            assertThatThrownBy(() -> feedReplyService.updateFeedReply(feedReplyId, request))
                    .isInstanceOf(FeedReplyException.class)
                    .hasMessage(FeedReplyErrorCode.INVALID_FEED_REPLY_CONTENT.getMessage());
        }

        @Test
        @DisplayName("답글이 존재하지 않으면 예외 발생")
        void updateFeed_fail_replyNotFound() {
            // given
            Long feedReplyId = 1L;
            FeedReplyUpdateRequest request = new FeedReplyUpdateRequest("수정내용", Collections.emptyList());

            when(feedReplyRepository.getById(feedReplyId)).thenThrow(new FeedReplyException(FeedReplyErrorCode.FEED_REPLY_NOT_FOUND));

            // when & then
            assertThatThrownBy(() -> feedReplyService.updateFeedReply(feedReplyId, request))
                    .isInstanceOf(FeedReplyException.class)
                    .hasMessage(FeedReplyErrorCode.FEED_REPLY_NOT_FOUND.getMessage());
        }
    }
} 