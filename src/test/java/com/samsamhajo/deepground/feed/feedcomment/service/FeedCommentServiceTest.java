package com.samsamhajo.deepground.feed.feedcomment.service;

import com.samsamhajo.deepground.feed.feed.entity.Feed;
import com.samsamhajo.deepground.feed.feed.repository.FeedRepository;
import com.samsamhajo.deepground.feed.feedcomment.entity.FeedComment;
import com.samsamhajo.deepground.feed.feedcomment.exception.FeedCommentErrorCode;
import com.samsamhajo.deepground.feed.feedcomment.exception.FeedCommentException;
import com.samsamhajo.deepground.feed.feedcomment.model.FeedCommentCreateRequest;
import com.samsamhajo.deepground.feed.feedcomment.model.FeedCommentUpdateRequest;
import com.samsamhajo.deepground.feed.feedcomment.model.FetchFeedCommentResponse;
import com.samsamhajo.deepground.feed.feedcomment.model.FetchFeedCommentsResponse;
import com.samsamhajo.deepground.feed.feedcomment.repository.FeedCommentRepository;
import com.samsamhajo.deepground.feed.feedreply.service.FeedReplyService;
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
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FeedCommentServiceTest {

    @InjectMocks
    private FeedCommentService feedCommentService;

    @Mock
    private FeedCommentRepository feedCommentRepository;

    @Mock
    private FeedCommentMediaService feedCommentMediaService;

    @Mock
    private FeedRepository feedRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private FeedCommentLikeService feedCommentLikeService;

    @Mock
    private FeedReplyService feedReplyService;

    private Member member = Member.createLocalMember("test@test.com", "password123", "테스트유저");
    private Feed feed = Feed.of("테스트 피드 내용", member);

    @Test
    @DisplayName("피드 댓글 생성 성공 테스트")
    void createFeedComment_Success() {
        // given
        String content = "테스트 댓글 내용";
        Long memberId = 1L;
        Long feedId = 1L;

        MockMultipartFile mockImage = new MockMultipartFile(
                "image",
                "test.jpg",
                "image/jpeg",
                "테스트 이미지 데이터".getBytes()
        );

        FeedCommentCreateRequest request = new FeedCommentCreateRequest(feedId, content, List.of(mockImage));

        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
        given(feedRepository.getById(feedId)).willReturn(feed);
        given(feedCommentRepository.save(any(FeedComment.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        FeedComment createdComment = feedCommentService.createFeedComment(request, memberId);

        // then
        assertThat(createdComment).isNotNull();
        assertThat(createdComment.getContent()).isEqualTo(content);
        assertThat(createdComment.getMember()).isEqualTo(member);
        assertThat(createdComment.getFeed()).isEqualTo(feed);

        verify(feedCommentMediaService).createFeedCommentMedia(any(FeedComment.class), any());
    }

    @Test
    @DisplayName("피드 댓글 생성 실패 테스트 - 빈 내용")
    void createFeedComment_Fail_EmptyContent() {
        // given
        String emptyContent = "";
        Long memberId = 1L;
        Long feedId = 1L;

        FeedCommentCreateRequest request = new FeedCommentCreateRequest(feedId, emptyContent, null);

        // when & then
        assertThatThrownBy(() -> feedCommentService.createFeedComment(request, memberId))
                .isInstanceOf(FeedCommentException.class)
                .hasFieldOrPropertyWithValue("errorCode", FeedCommentErrorCode.INVALID_FEED_COMMENT_CONTENT);
    }

    @Test
    @DisplayName("피드 댓글 생성 실패 테스트 - 존재하지 않는 회원")
    void createFeedComment_Fail_MemberNotFound() {
        // given
        String content = "테스트 댓글 내용";
        Long nonExistentMemberId = 999L;
        Long feedId = 1L;

        FeedCommentCreateRequest request = new FeedCommentCreateRequest(feedId, content, null);

        given(memberRepository.findById(nonExistentMemberId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> feedCommentService.createFeedComment(request, nonExistentMemberId))
                .isInstanceOf(MemberException.class)
                .hasFieldOrPropertyWithValue("errorCode", MemberErrorCode.INVALID_MEMBER_ID);
    }

    @Test
    @DisplayName("피드 댓글 생성 실패 테스트 - 존재하지 않는 피드")
    void createFeedComment_Fail_FeedNotFound() {
        // given
        String content = "테스트 댓글 내용";
        Long memberId = 1L;
        Long nonExistentFeedId = 999L;

        FeedCommentCreateRequest request = new FeedCommentCreateRequest(nonExistentFeedId, content, null);

        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
        given(feedRepository.getById(nonExistentFeedId))
                .willThrow(new FeedCommentException(FeedCommentErrorCode.FEED_COMMENT_NOT_FOUND));

        // when & then
        assertThatThrownBy(() -> feedCommentService.createFeedComment(request, memberId))
                .isInstanceOf(FeedCommentException.class)
                .hasFieldOrPropertyWithValue("errorCode", FeedCommentErrorCode.FEED_COMMENT_NOT_FOUND);
    }

    @Test
    @DisplayName("피드 댓글 수정 성공 테스트")
    void updateFeedComment_Success() {
        // given
        Long feedCommentId = 1L;
        String originalContent = "원본 댓글 내용";
        String updatedContent = "수정된 댓글 내용";

        FeedComment feedComment = FeedComment.of(originalContent, feed, member);

        MockMultipartFile mockImage = new MockMultipartFile(
                "image",
                "test.jpg",
                "image/jpeg",
                "테스트 이미지 데이터".getBytes()
        );

        FeedCommentUpdateRequest request = new FeedCommentUpdateRequest(updatedContent, List.of(mockImage));

        given(feedCommentRepository.getById(feedCommentId)).willReturn(feedComment);

        // when
        FeedComment updatedComment = feedCommentService.updateFeedComment(feedCommentId, request);

        // then
        assertThat(updatedComment).isNotNull();
        assertThat(updatedComment.getContent()).isEqualTo(updatedContent);
        assertThat(updatedComment.getMember()).isEqualTo(member);
        assertThat(updatedComment.getFeed()).isEqualTo(feed);

        verify(feedCommentMediaService).updateFeedCommentMedia(any(FeedComment.class), any());
    }

    @Test
    @DisplayName("피드 댓글 수정 실패 테스트 - 빈 내용")
    void updateFeedComment_Fail_EmptyContent() {
        // given
        Long feedCommentId = 1L;
        String emptyContent = "";

        FeedCommentUpdateRequest request = new FeedCommentUpdateRequest(emptyContent, null);

        // when & then
        assertThatThrownBy(() -> feedCommentService.updateFeedComment(feedCommentId, request))
                .isInstanceOf(FeedCommentException.class)
                .hasFieldOrPropertyWithValue("errorCode", FeedCommentErrorCode.INVALID_FEED_COMMENT_CONTENT);

        verify(feedCommentRepository, never()).getById(any());
    }

    @Test
    @DisplayName("피드 댓글 수정 실패 테스트 - 존재하지 않는 댓글")
    void updateFeedComment_Fail_CommentNotFound() {
        // given
        Long nonExistentCommentId = 999L;
        String updatedContent = "수정된 댓글 내용";

        FeedCommentUpdateRequest request = new FeedCommentUpdateRequest(updatedContent, null);

        given(feedCommentRepository.getById(nonExistentCommentId))
                .willThrow(new FeedCommentException(FeedCommentErrorCode.FEED_COMMENT_NOT_FOUND));

        // when & then
        assertThatThrownBy(() -> feedCommentService.updateFeedComment(nonExistentCommentId, request))
                .isInstanceOf(FeedCommentException.class)
                .hasFieldOrPropertyWithValue("errorCode", FeedCommentErrorCode.FEED_COMMENT_NOT_FOUND);
    }

    @Test
    @DisplayName("피드 댓글 삭제 성공 테스트")
    void deleteFeedComment_Success() {
        // given
        Long feedCommentId = 1L;

        // when
        feedCommentService.deleteFeedCommentId(feedCommentId);

        // then
        verify(feedReplyService).deleteAllByFeedCommentId(feedCommentId);
        verify(feedCommentMediaService).deleteAllByFeedCommentId(feedCommentId);
        verify(feedCommentLikeService).deleteAllByFeedCommentId(feedCommentId);
        verify(feedCommentRepository).deleteById(feedCommentId);
    }

    @Test
    @DisplayName("피드 댓글 목록 조회 성공 테스트")
    void getFeedComments_Success() {
        // given
        Long feedId = 1L;
        List<FeedComment> feedComments = List.of(
            FeedComment.of("댓글1", feed, member),
            FeedComment.of("댓글2", feed, member)
        );

        given(feedCommentRepository.findAllByFeedId(feedId)).willReturn(feedComments);
        given(feedCommentMediaService.getFeedCommentMediaIds(any())).willReturn(List.of(1L, 2L));
        given(feedReplyService.countFeedRepliesByFeedCommentId(any())).willReturn(2);
        given(feedCommentLikeService.countFeedCommentLikeByFeedId(any())).willReturn(3);

        // when
        FetchFeedCommentsResponse response = feedCommentService.getFeedComments(feedId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getFeedComments()).hasSize(2);
        
        FetchFeedCommentResponse firstComment = response.getFeedComments().get(0);
        assertThat(firstComment.getContent()).isEqualTo("댓글1");
        assertThat(firstComment.getMemberId()).isEqualTo(member.getId());
        assertThat(firstComment.getMemberName()).isEqualTo(member.getNickname());
        assertThat(firstComment.getMediaIds()).hasSize(2);
        assertThat(firstComment.getReplyCount()).isEqualTo(2);
        assertThat(firstComment.getLikeCount()).isEqualTo(3);
    }

    @Test
    @DisplayName("피드 댓글 목록 조회 실패 테스트 - 존재하지 않는 피드")
    void getFeedComments_Fail_FeedNotFound() {
        // given
        Long nonExistentFeedId = 999L;

        given(feedCommentRepository.findAllByFeedId(nonExistentFeedId))
            .willThrow(new FeedCommentException(FeedCommentErrorCode.FEED_COMMENT_NOT_FOUND));

        // when & then
        assertThatThrownBy(() -> feedCommentService.getFeedComments(nonExistentFeedId))
            .isInstanceOf(FeedCommentException.class)
            .hasFieldOrPropertyWithValue("errorCode", FeedCommentErrorCode.FEED_COMMENT_NOT_FOUND);
    }

    @Test
    @DisplayName("피드 댓글 목록 조회 실패 테스트 - 데이터베이스 오류")
    void getFeedComments_Fail_DatabaseError() {
        // given
        Long feedId = 1L;

        given(feedCommentRepository.findAllByFeedId(feedId))
            .willThrow(new RuntimeException("데이터베이스 연결 오류"));

        // when & then
        assertThatThrownBy(() -> feedCommentService.getFeedComments(feedId))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("데이터베이스 연결 오류");
    }
} 