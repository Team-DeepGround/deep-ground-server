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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeedReplyService {

    private final FeedCommentRepository feedCommentRepository;
    private final FeedReplyRepository feedReplyRepository;
    private final FeedReplyMediaService feedReplyMediaService;
    private final MemberRepository memberRepository;

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

    private void saveFeedReplyMedia(FeedReplyCreateRequest request, FeedReply feedReply) {
        feedReplyMediaService.createFeedReplyMedia(feedReply, request.getImages());
    }
} 