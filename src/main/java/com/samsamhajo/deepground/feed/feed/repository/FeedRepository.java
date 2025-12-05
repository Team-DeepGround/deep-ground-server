package com.samsamhajo.deepground.feed.feed.repository;

import com.samsamhajo.deepground.feed.feed.entity.Feed;
import com.samsamhajo.deepground.feed.feed.exception.FeedErrorCode;
import com.samsamhajo.deepground.feed.feed.exception.FeedException;
import com.samsamhajo.deepground.feed.feed.model.FetchFeedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface FeedRepository extends JpaRepository<Feed, Long>,FeedRepositoryCustom {

    default Feed getById(Long id) {
        return findById(id).orElseThrow(() -> new FeedException(FeedErrorCode.FEED_NOT_FOUND));
    }

    Page<Feed> findAllByMemberId(Pageable pageable, Long memberId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Feed f " +
            "SET f.likeCount = (" +
            "   SELECT COUNT(fl) " +
            "   FROM FeedLike fl " +
            "   WHERE fl.feed = f) " +
            "WHERE f.id = :feedId")
    void updateCountFeedLikeByFeedId(@Param("feedId") Long feedId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Feed f " +
            "SET f.commentCount = (" +
            "   SELECT COUNT(fc) " +
            "   FROM FeedComment fc " +
            "   WHERE fc.feed = f) " +
            "WHERE f.id = :feedId")
    void updateCountFeedCommentByFeedId(@Param("feedId") Long feedId);

    @Modifying
    @Query("UPDATE Feed f " +
            "SET f.sharedCount = (" +
            "   SELECT COUNT(sf)" +
            "   FROM SharedFeed sf" +
            "   WHERE sf.feed = f) " +
            "WHERE f.id= :feedId")
    void updateCountFeedSharedById(Long feedId);

}
