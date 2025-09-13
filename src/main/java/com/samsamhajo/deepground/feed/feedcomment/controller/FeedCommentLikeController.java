package com.samsamhajo.deepground.feed.feedcomment.controller;

import com.samsamhajo.deepground.auth.security.CustomUserDetails;
import com.samsamhajo.deepground.feed.feedcomment.exception.FeedCommentSuccessCode;
import com.samsamhajo.deepground.feed.feedcomment.service.FeedCommentLikeService;
import com.samsamhajo.deepground.global.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feed/comment")
@RequiredArgsConstructor
public class FeedCommentLikeController {

    private final FeedCommentLikeService feedCommentLikeService;

    @PostMapping("/{feedCommentId}/like")
    public ResponseEntity<SuccessResponse<?>> createFeedLike(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("feedCommentId") Long feedCommentId) {

        feedCommentLikeService.feedLikeIncrease(feedCommentId, userDetails.getMember());

        return ResponseEntity
                .ok(SuccessResponse.of(FeedCommentSuccessCode.FEED_COMMENT_LIKED));
    }

    @DeleteMapping("/{feedCommentId}/like")
    public ResponseEntity<SuccessResponse<?>> deleteFeedLike(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("feedCommentId") Long feedCommentId) {

        feedCommentLikeService.feedLikeDecrease(feedCommentId, userDetails.getMember().getId());

        return ResponseEntity
                .ok(SuccessResponse.of(FeedCommentSuccessCode.FEED_COMMENT_DISLIKED));
    }

}