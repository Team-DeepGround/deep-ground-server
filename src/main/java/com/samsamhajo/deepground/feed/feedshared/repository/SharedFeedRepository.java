package com.samsamhajo.deepground.feed.feedshared.repository;

import com.samsamhajo.deepground.feed.feedshared.entity.SharedFeed;
import com.samsamhajo.deepground.feed.feedshared.exception.SharedFeedErrorCode;
import com.samsamhajo.deepground.feed.feedshared.exception.SharedFeedException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SharedFeedRepository extends JpaRepository<SharedFeed, Long> {

    int countAllByOriginFeedId(Long originFeedId);

    Optional<SharedFeed> findByFeedId(Long feedId);

    default SharedFeed getByFeedId(Long feedId){
        return findByFeedId(feedId)
                .orElseThrow(() -> new SharedFeedException(SharedFeedErrorCode.SHARED_FEED_NOT_FOUND));
    }

    default SharedFeed getOrNullByFeedId(Long feedId){
        return findByFeedId(feedId).orElse(null);
    }
} 