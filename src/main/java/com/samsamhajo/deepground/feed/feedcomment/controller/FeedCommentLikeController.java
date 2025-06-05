package com.samsamhajo.deepground.feed.feedcomment.controller;

import com.samsamhajo.deepground.feed.feedcomment.exception.FeedCommentSuccessCode;
import com.samsamhajo.deepground.feed.feedcomment.service.FeedCommentLikeService;
import com.samsamhajo.deepground.global.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feed/comment")
@RequiredArgsConstructor
public class FeedCommentLikeController {

    private final FeedCommentLikeService feedCommentLikeService;

    private final Long DEV_MEMBER_ID = 1L;

    @PostMapping("/{feedCommentId}/like")
    public ResponseEntity<SuccessResponse<?>> createFeedLike(
            @PathVariable("feedCommentId") Long feedCommentId) {

        feedCommentLikeService.feedLikeIncrease(feedCommentId, DEV_MEMBER_ID);

        return ResponseEntity
                .ok(SuccessResponse.of(FeedCommentSuccessCode.FEED_COMMENT_LIKED));
    }

    @DeleteMapping("/{feedCommentId}/like")
    public ResponseEntity<SuccessResponse<?>> deleteFeedLike(
            @PathVariable("feedCommentId") Long feedCommentId) {

        feedCommentLikeService.feedLikeDecrease(feedCommentId, DEV_MEMBER_ID);

        return ResponseEntity
                .ok(SuccessResponse.of(FeedCommentSuccessCode.FEED_COMMENT_DISLIKED));
    }

}