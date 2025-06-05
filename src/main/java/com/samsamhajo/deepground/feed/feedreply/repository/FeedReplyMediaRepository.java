package com.samsamhajo.deepground.feed.feedreply.repository;

import com.samsamhajo.deepground.feed.feedreply.entity.FeedReplyMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedReplyMediaRepository extends JpaRepository<FeedReplyMedia, Long> {
    List<FeedReplyMedia> findAllByFeedReplyId(Long feedReplyId);

    void deleteAllByFeedReplyId(Long feedReplyId);
}