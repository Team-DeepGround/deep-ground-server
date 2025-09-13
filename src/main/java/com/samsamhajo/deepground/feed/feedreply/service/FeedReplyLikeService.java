package com.samsamhajo.deepground.feed.feedreply.service;

import com.samsamhajo.deepground.feed.feedreply.entity.FeedReply;
import com.samsamhajo.deepground.feed.feedreply.entity.FeedReplyLike;
import com.samsamhajo.deepground.feed.feedreply.exception.FeedReplyErrorCode;
import com.samsamhajo.deepground.feed.feedreply.exception.FeedReplyException;
import com.samsamhajo.deepground.feed.feedreply.repository.FeedReplyLikeRepository;
import com.samsamhajo.deepground.feed.feedreply.repository.FeedReplyRepository;
import com.samsamhajo.deepground.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedReplyLikeService {

    private final FeedReplyRepository feedReplyRepository;
    private final FeedReplyLikeRepository feedReplyLikeRepository;

    @Transactional
    public void feedReplyLikeIncrease(Long feedReplyId, Member member) {
        increaseValidate(feedReplyId, member.getId());

        FeedReply feedReply = feedReplyRepository.getById(feedReplyId);

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

    public boolean isLiked(Long feedReplyId, Long memberId) {
        return feedReplyLikeRepository.existsByFeedReplyIdAndMemberId(feedReplyId, memberId);
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