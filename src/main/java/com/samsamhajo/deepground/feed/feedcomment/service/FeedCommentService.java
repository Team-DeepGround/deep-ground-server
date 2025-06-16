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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeedCommentService {

    private final FeedCommentRepository feedCommentRepository;
    private final FeedCommentMediaService feedCommentMediaService;
    private final FeedRepository feedRepository;
    private final MemberRepository memberRepository;
    private final FeedReplyService feedReplyService;
    private final FeedCommentLikeService feedCommentLikeService;

    @Transactional
    public FeedComment createFeedComment(FeedCommentCreateRequest request, Long memberId) {
        if (!StringUtils.hasText(request.getContent())) {
            throw new FeedCommentException(FeedCommentErrorCode.INVALID_FEED_COMMENT_CONTENT);
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.INVALID_MEMBER_ID));

        Feed feed = feedRepository.getById(request.getFeedId());

        FeedComment feedComment = FeedComment.of(request.getContent(), feed, member);
        feedCommentRepository.save(feedComment);

        saveFeedCommentMedia(request, feedComment);

        return feedComment;
    }

    @Transactional
    public FeedComment updateFeedComment(Long feedCommentId, FeedCommentUpdateRequest request) {
        if (!StringUtils.hasText(request.getContent())) {
            throw new FeedCommentException(FeedCommentErrorCode.INVALID_FEED_COMMENT_CONTENT);
        }

        FeedComment feedComment = feedCommentRepository.getById(feedCommentId);

        // 피드 내용 업데이트
        feedComment.updateContent(request.getContent());

        // 미디어 업데이트
        feedCommentMediaService.updateFeedCommentMedia(feedComment, request.getImages());

        return feedComment;
    }

    @Transactional
    public void deleteFeedCommentId(Long feedCommentId) {
        // 관련 엔티티 모두 삭제
        deleteAllRelatedEntities(feedCommentId);

        // 피드 댓글 삭제
        feedCommentRepository.deleteById(feedCommentId);
    }

    @Transactional
    public void deleteFeedCommentByFeed(Long feedId) {
        List<FeedComment> feedComments = feedCommentRepository.findAllByFeedId(feedId);

        // 댓글과 연관된 데이터 모두 삭제
        feedComments.forEach(feedComment -> deleteAllRelatedEntities(feedComment.getId()));

        // 피드와 연결된 모든 댓글 삭제
        feedCommentRepository.deleteAll(feedComments);
    }

    public FetchFeedCommentsResponse getFeedComments(Long feedId, Long memberId) {
        return FetchFeedCommentsResponse.of(
                feedCommentRepository.findAllByFeedId(feedId).stream()
                        .map(feedComment -> FetchFeedCommentResponse.builder()
                                .feedCommentId(feedComment.getId())
                                .content(feedComment.getContent())
                                .createdAt(feedComment.getCreatedAt().toLocalDate())
                                .memberId(feedComment.getMember().getId())
                                .memberName(feedComment.getMember().getNickname())
                                .mediaIds(feedCommentMediaService.getFeedCommentMediaIds(feedComment.getId()))
                                .replyCount(feedReplyService.countFeedRepliesByFeedCommentId(feedComment.getId()))
                                .likeCount(feedCommentLikeService.countFeedCommentLikeByFeedId(feedComment.getId()))
                                .isLiked(feedCommentLikeService.isLiked(feedComment.getId(),memberId))
                                .build()).toList());
    }

    public int countFeedCommentsByFeedId(Long feedId) {
        return feedCommentRepository.countByFeedId(feedId);
    }

    private void deleteAllRelatedEntities(Long feedCommentId) {
        feedReplyService.deleteAllByFeedCommentId(feedCommentId);
        feedCommentMediaService.deleteAllByFeedCommentId(feedCommentId);
        feedCommentLikeService.deleteAllByFeedCommentId(feedCommentId);
    }

    private void saveFeedCommentMedia(FeedCommentCreateRequest request, FeedComment feedComment) {
        feedCommentMediaService.createFeedCommentMedia(feedComment, request.getImages());
    }
} 