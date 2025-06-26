package com.samsamhajo.deepground.feed.feedreply.controller;

import com.samsamhajo.deepground.auth.security.CustomUserDetails;
import com.samsamhajo.deepground.feed.feedreply.exception.FeedReplySuccessCode;
import com.samsamhajo.deepground.feed.feedreply.service.FeedReplyLikeService;
import com.samsamhajo.deepground.global.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feed/comment/reply")
@RequiredArgsConstructor
public class FeedReplyLikeController {

    private final FeedReplyLikeService feedReplyLikeService;

    @PostMapping("/{feedReplyId}/like")
    public ResponseEntity<SuccessResponse<?>> createFeedReplyLike(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("feedReplyId") Long feedReplyId) {

        feedReplyLikeService.feedReplyLikeIncrease(feedReplyId, userDetails.getMember());

        return ResponseEntity
                .ok(SuccessResponse.of(FeedReplySuccessCode.FEED_REPLY_LIKED));
    }

    @DeleteMapping("/{feedReplyId}/like")
    public ResponseEntity<SuccessResponse<?>> deleteFeedReplyLike(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("feedReplyId") Long feedReplyId) {

        feedReplyLikeService.feedReplyLikeDecrease(feedReplyId, userDetails.getMember().getId());

        return ResponseEntity
                .ok(SuccessResponse.of(FeedReplySuccessCode.FEED_REPLY_DISLIKED));
    }
} 