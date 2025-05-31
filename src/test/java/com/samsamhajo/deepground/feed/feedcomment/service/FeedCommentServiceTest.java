package com.samsamhajo.deepground.feed.feedcomment.service;

import com.samsamhajo.deepground.feed.feed.entity.Feed;
import com.samsamhajo.deepground.feed.feed.repository.FeedRepository;
import com.samsamhajo.deepground.feed.feedcomment.entity.FeedComment;
import com.samsamhajo.deepground.feed.feedcomment.exception.FeedCommentErrorCode;
import com.samsamhajo.deepground.feed.feedcomment.exception.FeedCommentException;
import com.samsamhajo.deepground.feed.feedcomment.model.FeedCommentCreateRequest;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
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
} 