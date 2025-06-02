package com.samsamhajo.deepground.feed.feedcomment.service;

import com.samsamhajo.deepground.feed.feed.entity.Feed;
import com.samsamhajo.deepground.feed.feed.repository.FeedRepository;
import com.samsamhajo.deepground.feed.feedcomment.entity.FeedComment;
import com.samsamhajo.deepground.feed.feedcomment.exception.FeedCommentErrorCode;
import com.samsamhajo.deepground.feed.feedcomment.exception.FeedCommentException;
import com.samsamhajo.deepground.feed.feedcomment.model.FeedCommentCreateRequest;
import com.samsamhajo.deepground.feed.feedcomment.model.FeedCommentUpdateRequest;
import com.samsamhajo.deepground.feed.feedcomment.repository.FeedCommentRepository;
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
public class FeedCommentService {

    private final FeedCommentRepository feedCommentRepository;
    private final FeedCommentMediaService feedCommentMediaService;
    private final FeedRepository feedRepository;
    private final MemberRepository memberRepository;

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
    public FeedComment updateFeed(Long feedCommentId, FeedCommentUpdateRequest request) {
        if (!StringUtils.hasText(request.getContent())) {
            throw new FeedCommentException(FeedCommentErrorCode.INVALID_FEED_COMMENT_CONTENT);
        }

        FeedComment feedComment = feedCommentRepository.getById(feedCommentId);

        // 피드 내용 업데이트
        feedComment.updateContent(request.getContent());

        // 미디어 업데이트
        feedCommentMediaService.updateFeedCommentMedia(feedComment, request);

        return feedComment;
    }

    private void saveFeedCommentMedia(FeedCommentCreateRequest request, FeedComment feedComment) {
        feedCommentMediaService.createFeedCommentMedia(feedComment, request.getImages());
    }
} 