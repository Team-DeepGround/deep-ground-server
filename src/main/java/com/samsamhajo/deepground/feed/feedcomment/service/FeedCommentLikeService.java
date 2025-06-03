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
    private final MemberRepository memberRepository;
    private final FeedCommentLikeRepository feedCommentLikeRepository;

    @Transactional
    public void feedLikeIncrease(Long feedId, Long memberId) {
        increaseValidate(feedId, memberId);

        FeedComment feedComment = feedCommentRepository.getById(feedId);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.INVALID_MEMBER_ID));

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

    public int countFeedCommentLikeByFeedId(Long feedId) {
        return feedCommentLikeRepository.countByFeedCommentId(feedId);
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
