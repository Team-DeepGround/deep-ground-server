package com.samsamhajo.deepground.feed.feedcomment.repository;

import com.samsamhajo.deepground.feed.feedcomment.entity.FeedCommentLike;
import com.samsamhajo.deepground.feed.feedcomment.exception.FeedCommentErrorCode;
import com.samsamhajo.deepground.feed.feedcomment.exception.FeedCommentException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface FeedCommentLikeRepository extends JpaRepository<FeedCommentLike, Long> {

    int countByFeedCommentId(Long feedCommentId);

    boolean existsByFeedCommentIdAndMemberId(Long feedCommentId, Long memberId);

    Optional<FeedCommentLike> findByFeedCommentIdAndMemberId(Long feedCommentId, Long memberId);

    default FeedCommentLike getByFeedCommentIdAndMemberId(Long feedCommentId, Long memberId) {
        return findByFeedCommentIdAndMemberId(feedCommentId, memberId)
                .orElseThrow(() -> new FeedCommentException(FeedCommentErrorCode.FEED_COMMENT_LIKE_NOT_FOUND));
    }
}
