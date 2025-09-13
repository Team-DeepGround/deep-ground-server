package com.samsamhajo.deepground.feed.feedcomment.service;


import com.samsamhajo.deepground.feed.feedcomment.entity.FeedComment;
import com.samsamhajo.deepground.feed.feedcomment.entity.FeedCommentLike;
import com.samsamhajo.deepground.feed.feedcomment.exception.FeedCommentErrorCode;
import com.samsamhajo.deepground.feed.feedcomment.exception.FeedCommentException;
import com.samsamhajo.deepground.feed.feedcomment.repository.FeedCommentLikeRepository;
import com.samsamhajo.deepground.feed.feedcomment.repository.FeedCommentRepository;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.exception.MemberErrorCode;
import com.samsamhajo.deepground.member.exception.MemberException;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedCommentLikeService {

    private final FeedCommentRepository feedCommentRepository;
    private final FeedCommentLikeRepository feedCommentLikeRepository;

    @Transactional
    public void feedLikeIncrease(Long feedId, Member member) {
        increaseValidate(feedId, member.getId());

        FeedComment feedComment = feedCommentRepository.getById(feedId);

        FeedCommentLike feedCommentLike = FeedCommentLike.of(feedComment, member);

        feedCommentLikeRepository.save(feedCommentLike);
    }

    @Transactional
    public void feedLikeDecrease(Long feedId, Long memberId) {
        // 이미 0 이거나 음수인 경우 취소하려 할 때, 예외 발생 
        validateDecrease(feedId);

        FeedCommentLike feedCommentLike = feedCommentLikeRepository.getByFeedCommentIdAndMemberId(feedId, memberId);

        feedCommentLikeRepository.delete(feedCommentLike);
    }

    public int countFeedCommentLikeByFeedId(Long feedCommentId) {
        return feedCommentLikeRepository.countByFeedCommentId(feedCommentId);
    }

    public void deleteAllByFeedCommentId(Long feedCommentId) {
        feedCommentLikeRepository.deleteAllByFeedCommentId(feedCommentId);
    }

    public boolean isLiked(Long feedCommentId, Long memberId) {
        return feedCommentLikeRepository.existsByFeedCommentIdAndMemberId(feedCommentId, memberId);
    }

    private void validateDecrease(Long feedId) {
        if (countFeedCommentLikeByFeedId(feedId) <= 0) {
            throw new FeedCommentException(FeedCommentErrorCode.FEED_LIKE_MINUS_NOT_ALLOWED);
        }
    }

    private void increaseValidate(Long feedCommentId, Long memberId) {
        if (feedCommentLikeRepository.existsByFeedCommentIdAndMemberId(feedCommentId, memberId)) {
            throw new FeedCommentException(FeedCommentErrorCode.FEED_COMMENT_LIKE_ALREADY_EXISTS);
        }
    }
}
