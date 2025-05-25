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

@Service
@RequiredArgsConstructor
public class FeedLikeService {

    private final FeedRepository feedRepository;
    private final MemberRepository memberRepository;
    private final FeedLikeRepository feedLikeRepository;

    public void feedLikeIncrease(Long feedId, Long memberId) {
        validate(feedId, memberId);

        Feed feed = feedRepository.getById(feedId);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.INVALID_MEMBER_ID));

        FeedLike feedLike = FeedLike.of(feed, member);

        feedLikeRepository.save(feedLike);
    }

    public int countFeedLikeByFeedId(Long feedId) {
        return feedLikeRepository.countByFeedId(feedId);
    }

    private void validate(Long feedId, Long memberId) {
        if (feedLikeRepository.existsByFeedIdAndMemberId(feedId, memberId)) {
            throw new FeedException(FeedErrorCode.FEED_LIKE_ALREADY_EXISTS);
        }
    }
}
