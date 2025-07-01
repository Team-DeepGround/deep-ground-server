package com.samsamhajo.deepground.feed.feed.repository;

import com.samsamhajo.deepground.feed.feed.entity.Feed;
import com.samsamhajo.deepground.feed.feed.exception.FeedErrorCode;
import com.samsamhajo.deepground.feed.feed.exception.FeedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {
    default Feed getById(Long id) {
        return findById(id).orElseThrow(() -> new FeedException(FeedErrorCode.FEED_NOT_FOUND));
    }

    Page<Feed> findAllByMemberId(Pageable pageable, Long memberId);
}
