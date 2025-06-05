package com.samsamhajo.deepground.feed.feedreply.repository;

import com.samsamhajo.deepground.feed.feedreply.entity.FeedReply;
import com.samsamhajo.deepground.feed.feedreply.exception.FeedReplyErrorCode;
import com.samsamhajo.deepground.feed.feedreply.exception.FeedReplyException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedReplyRepository extends JpaRepository<FeedReply, Long> {
    default FeedReply getById(Long feedReplyId) {
        return findById(feedReplyId).orElseThrow(() -> new FeedReplyException(FeedReplyErrorCode.FEED_REPLY_NOT_FOUND));
    }

    List<FeedReply> findAllByFeedCommentId(Long feedCommentId);
} 