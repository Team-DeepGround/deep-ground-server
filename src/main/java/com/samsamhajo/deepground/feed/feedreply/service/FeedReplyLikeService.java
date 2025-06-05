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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedReplyLikeService {

    private final FeedReplyRepository feedReplyRepository;
    private final MemberRepository memberRepository;
    private final FeedReplyLikeRepository feedReplyLikeRepository;

    @Transactional
    public void feedReplyLikeIncrease(Long feedReplyId, Long memberId) {
        increaseValidate(feedReplyId, memberId);

        FeedReply feedReply = feedReplyRepository.getById(feedReplyId);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.INVALID_MEMBER_ID));

        FeedReplyLike feedReplyLike = FeedReplyLike.of(feedReply, member);

        feedReplyLikeRepository.save(feedReplyLike);
    }

    @Transactional
    public void feedReplyLikeDecrease(Long feedReplyId, Long memberId) {
        validateDecrease(feedReplyId);

        FeedReplyLike feedReplyLike = feedReplyLikeRepository.getByFeedReplyIdAndMemberId(feedReplyId, memberId);

        feedReplyLikeRepository.delete(feedReplyLike);
    }

    public int countFeedReplyLikeByFeedReplyId(Long feedReplyId) {
        return feedReplyLikeRepository.countByFeedReplyId(feedReplyId);
    }

    public void deleteAllByFeedReplyId(Long feedReplyId) {
        feedReplyLikeRepository.deleteAllByFeedReplyId(feedReplyId);
    }

    private void validateDecrease(Long feedReplyId) {
        if (countFeedReplyLikeByFeedReplyId(feedReplyId) <= 0) {
            throw new FeedReplyException(FeedReplyErrorCode.FEED_LIKE_MINUS_NOT_ALLOWED);
        }
    }

    private void increaseValidate(Long feedReplyId, Long memberId) {
        if (feedReplyLikeRepository.existsByFeedReplyIdAndMemberId(feedReplyId, memberId)) {
            throw new FeedReplyException(FeedReplyErrorCode.FEED_REPLY_LIKE_ALREADY_EXISTS);
        }
    }
} 