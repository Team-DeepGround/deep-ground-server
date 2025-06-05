package com.samsamhajo.deepground.feed.feedreply.repository;

import com.samsamhajo.deepground.feed.feedreply.entity.FeedReplyLike;
import com.samsamhajo.deepground.feed.feedreply.exception.FeedReplyErrorCode;
import com.samsamhajo.deepground.feed.feedreply.exception.FeedReplyException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeedReplyLikeRepository extends JpaRepository<FeedReplyLike, Long> {

    int countByFeedReplyId(Long feedReplyId);

    boolean existsByFeedReplyIdAndMemberId(Long feedReplyId, Long memberId);

    Optional<FeedReplyLike> findByFeedReplyIdAndMemberId(Long feedReplyId, Long memberId);

    default FeedReplyLike getByFeedReplyIdAndMemberId(Long feedReplyId, Long memberId) {
        return findByFeedReplyIdAndMemberId(feedReplyId, memberId)
                .orElseThrow(() -> new FeedReplyException(FeedReplyErrorCode.FEED_REPLY_LIKE_NOT_FOUND));
    }

    void deleteAllByFeedReplyId(Long feedReplyId);
} 