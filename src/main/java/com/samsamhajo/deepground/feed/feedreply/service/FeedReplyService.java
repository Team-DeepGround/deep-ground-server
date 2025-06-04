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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeedReplyService {

    private final FeedCommentRepository feedCommentRepository;
    private final FeedReplyRepository feedReplyRepository;
    private final MemberRepository memberRepository;
    private final FeedReplyMediaService feedReplyMediaService;
    private final FeedReplyLikeService feedReplyLikeService;

    @Transactional
    public FeedComment createFeedReply(FeedReplyCreateRequest request, Long memberId) {
        if (!StringUtils.hasText(request.getContent())) {
            throw new FeedCommentException(FeedCommentErrorCode.INVALID_FEED_COMMENT_CONTENT);
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.INVALID_MEMBER_ID));

        FeedComment feedComment = feedCommentRepository.getById(request.getFeedCommentId());

        FeedReply feedReply = FeedReply.of(request.getContent(), feedComment, member);

        feedReplyRepository.save(feedReply);

        saveFeedReplyMedia(request, feedReply);

        return feedComment;
    }

    @Transactional
    public FeedReply updateFeedReply(Long feedReplyId, FeedReplyUpdateRequest request) {
        if (!StringUtils.hasText(request.getContent())) {
            throw new FeedReplyException(FeedReplyErrorCode.INVALID_FEED_REPLY_CONTENT);
        }

        FeedReply feedReply = feedReplyRepository.getById(feedReplyId);

        // 피드 내용 업데이트
        feedReply.updateContent(request.getContent());

        // 미디어 업데이트
        feedReplyMediaService.updateFeedReplyMedia(feedReply, request.getImages());

        return feedReply;
    }

    @Transactional
    public void deleteFeedReplyId(Long feedReplyId) {
        // 연관관계 엔티티 모두 삭제
        deleteRelatedEntities(feedReplyId);

        // 피드 삭제
        feedReplyRepository.deleteById(feedReplyId);
    }

    @Transactional
    public void deleteAllByFeedCommentId(Long feedCommentId) {
        List<FeedReply> feedReplies = feedReplyRepository.findAllByFeedCommentId(feedCommentId);

        // 피드 답글에 관련된 모든 요소 삭제
        feedReplies.forEach(feedReply -> deleteRelatedEntities(feedReply.getId()));

        // 피드 댓글에 연결된 모든 답글 삭제
        feedReplyRepository.deleteAll(feedReplies);
    }

    private void deleteRelatedEntities(Long feedReplyId) {
        // 피드 댓글에 연결된 모든 미디어 삭제
        feedReplyMediaService.deleteAllByFeedReplyId(feedReplyId);

        // 피드 댓글에 관련된 좋아요 모두 삭제
        feedReplyLikeService.deleteAllByFeedReplyId(feedReplyId);
    }

    private void saveFeedReplyMedia(FeedReplyCreateRequest request, FeedReply feedReply) {
        feedReplyMediaService.createFeedReplyMedia(feedReply, request.getImages());
    }
} 