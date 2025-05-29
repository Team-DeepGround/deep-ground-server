package com.samsamhajo.deepground.feed.feed.repository;

import com.samsamhajo.deepground.feed.feed.entity.FeedLike;
import com.samsamhajo.deepground.feed.feed.exception.FeedErrorCode;
import com.samsamhajo.deepground.feed.feed.exception.FeedException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface FeedLikeRepository extends JpaRepository<FeedLike, Long> {

    int countByFeedId(Long feedId);

    boolean existsByFeedIdAndMemberId(Long feedId, Long memberId);

    List<FeedLike> findByFeedId(Long feedId);

    Optional<FeedLike> findByFeedIdAndMemberId(Long feed, Long memberId);

    default FeedLike getByFeedIdAndMemberId(Long feedId, Long memberId){
        return findByFeedIdAndMemberId(feedId, memberId)
                .orElseThrow(()->new FeedException(FeedErrorCode.FEED_LIKE_NOT_FOUND));
    }
}
