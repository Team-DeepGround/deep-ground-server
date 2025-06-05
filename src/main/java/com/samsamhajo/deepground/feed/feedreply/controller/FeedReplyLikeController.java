package com.samsamhajo.deepground.feed.feedreply.controller;

import com.samsamhajo.deepground.feed.feedreply.exception.FeedReplySuccessCode;
import com.samsamhajo.deepground.feed.feedreply.service.FeedReplyLikeService;
import com.samsamhajo.deepground.global.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feed/comment/reply")
@RequiredArgsConstructor
public class FeedReplyLikeController {

    private final FeedReplyLikeService feedReplyLikeService;
    private final Long DEV_MEMBER_ID = 1L;

    @PostMapping("/{feedReplyId}/like")
    public ResponseEntity<SuccessResponse<?>> createFeedReplyLike(
            @PathVariable("feedReplyId") Long feedReplyId) {

        feedReplyLikeService.feedReplyLikeIncrease(feedReplyId, DEV_MEMBER_ID);

        return ResponseEntity
                .ok(SuccessResponse.of(FeedReplySuccessCode.FEED_REPLY_LIKED));
    }

    @DeleteMapping("/{feedReplyId}/like")
    public ResponseEntity<SuccessResponse<?>> deleteFeedReplyLike(
            @PathVariable("feedReplyId") Long feedReplyId) {

        feedReplyLikeService.feedReplyLikeDecrease(feedReplyId, DEV_MEMBER_ID);

        return ResponseEntity
                .ok(SuccessResponse.of(FeedReplySuccessCode.FEED_REPLY_DISLIKED));
    }
} 