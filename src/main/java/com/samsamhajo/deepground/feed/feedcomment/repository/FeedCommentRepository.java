package com.samsamhajo.deepground.feed.feedcomment.repository;

import com.samsamhajo.deepground.feed.feedcomment.entity.FeedComment;
import com.samsamhajo.deepground.feed.feedcomment.exception.FeedCommentErrorCode;
import com.samsamhajo.deepground.feed.feedcomment.exception.FeedCommentException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedCommentRepository extends JpaRepository<FeedComment, Long> {
    default FeedComment getById(Long feedCommentId) {
        return findById(feedCommentId).orElseThrow(() -> new FeedCommentException(FeedCommentErrorCode.FEED_COMMENT_NOT_FOUND));
    }
    List<FeedComment> findAllByFeedId(Long feedId);

    int countByFeedId(Long feedId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE FeedComment fc" +
            "   SET fc.likeCount = (" +
            "   SELECT COUNT(fc)" +
            "   FROM FeedCommentLike fcl" +
            "   WHERE fcl.feedComment = fc)" +
            "   WHERE fc.id =: feedCommnetId")
    void updateCountCommentLikeById(Long feedCommentId);
}