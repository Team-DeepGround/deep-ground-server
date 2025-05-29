package com.samsamhajo.deepground.feed.feed.service;


import com.samsamhajo.deepground.feed.feed.entity.Feed;
import com.samsamhajo.deepground.feed.feed.entity.FeedLike;
import com.samsamhajo.deepground.feed.feed.exception.FeedErrorCode;
import com.samsamhajo.deepground.feed.feed.exception.FeedException;
import com.samsamhajo.deepground.feed.feed.repository.FeedLikeRepository;
import com.samsamhajo.deepground.feed.feed.repository.FeedRepository;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.exception.MemberErrorCode;
import com.samsamhajo.deepground.member.exception.MemberException;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedLikeService {

    private final FeedRepository feedRepository;
    private final MemberRepository memberRepository;
    private final FeedLikeRepository feedLikeRepository;

    @Transactional
    public void feedLikeIncrease(Long feedId, Long memberId) {
        increaseValidate(feedId, memberId);

        Feed feed = feedRepository.getById(feedId);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.INVALID_MEMBER_ID));

        FeedLike feedLike = FeedLike.of(feed, member);

        feedLikeRepository.save(feedLike);
    }

    @Transactional
    public void feedLikeDecrease(Long feedId, Long memberId) {
        // 이미 0 이거나 음수인 경우 취소하려 할 때, 예외 발생 
        validateDecrease(feedId);

        FeedLike feedLike = feedLikeRepository.getByFeedIdAndMemberId(feedId, memberId);

        feedLikeRepository.delete(feedLike);
    }

    public int countFeedLikeByFeedId(Long feedId) {
        return feedLikeRepository.countByFeedId(feedId);
    }

    private void validateDecrease(Long feedId) {
        if(countFeedLikeByFeedId(feedId) <= 0){
            throw new FeedException(FeedErrorCode.FEED_LIKE_MINUS_NOT_ALLOWED);
        }
    }

    private void increaseValidate(Long feedId, Long memberId) {
        if (feedLikeRepository.existsByFeedIdAndMemberId(feedId, memberId)) {
            throw new FeedException(FeedErrorCode.FEED_LIKE_ALREADY_EXISTS);
        }
    }
}
