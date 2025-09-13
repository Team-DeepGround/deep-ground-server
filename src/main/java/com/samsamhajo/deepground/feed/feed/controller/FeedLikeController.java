package com.samsamhajo.deepground.feed.feed.controller;

import com.samsamhajo.deepground.auth.security.CustomUserDetails;
import com.samsamhajo.deepground.feed.feed.entity.Feed;
import com.samsamhajo.deepground.feed.feed.exception.FeedSuccessCode;
import com.samsamhajo.deepground.feed.feed.service.FeedLikeService;
import com.samsamhajo.deepground.global.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feed")
@RequiredArgsConstructor
public class FeedLikeController {

    private final FeedLikeService feedLikeService;

    @PostMapping("/{feedId}/like")
    public ResponseEntity<SuccessResponse<Feed>> createFeedLike(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable ("feedId") Long feedId){

        feedLikeService.feedLikeIncrease(feedId, userDetails.getMember());

        return ResponseEntity
                .ok(SuccessResponse.of(FeedSuccessCode.FEED_LIKED));
    }

    @DeleteMapping("/{feedId}/like")
    public ResponseEntity<SuccessResponse<Feed>> deleteFeedLike(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable ("feedId") Long feedId){

        feedLikeService.feedLikeDecrease(feedId, userDetails.getMember().getId());

        return ResponseEntity
                .ok(SuccessResponse.of(FeedSuccessCode.FEED_UNLIKED));
    }

}