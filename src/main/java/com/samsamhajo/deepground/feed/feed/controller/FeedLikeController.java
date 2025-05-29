package com.samsamhajo.deepground.feed.feed.controller;

import com.samsamhajo.deepground.feed.feed.entity.Feed;
import com.samsamhajo.deepground.feed.feed.exception.FeedSuccessCode;
import com.samsamhajo.deepground.feed.feed.model.FeedCreateRequest;
import com.samsamhajo.deepground.feed.feed.model.FeedListResponse;
import com.samsamhajo.deepground.feed.feed.model.FeedUpdateRequest;
import com.samsamhajo.deepground.feed.feed.service.FeedLikeService;
import com.samsamhajo.deepground.feed.feed.service.FeedService;
import com.samsamhajo.deepground.global.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feed")
@RequiredArgsConstructor
public class FeedLikeController {

    private final FeedLikeService feedLikeService;

    private final Long DEV_MEMBER_ID = 1L;

    @PostMapping("/{feedId}/like")
    public ResponseEntity<SuccessResponse<Feed>> createFeedLike(
            @PathVariable ("feedId") Long feedId){

        feedLikeService.feedLikeIncrease(feedId, DEV_MEMBER_ID);

        return ResponseEntity
                .ok(SuccessResponse.of(FeedSuccessCode.FEED_LIKED));
    }

    @DeleteMapping("/{feedId}/like")
    public ResponseEntity<SuccessResponse<Feed>> deleteFeedLike(
            @PathVariable ("feedId") Long feedId){

        feedLikeService.feedLikeDecrease(feedId, DEV_MEMBER_ID);

        return ResponseEntity
                .ok(SuccessResponse.of(FeedSuccessCode.FEED_UNLIKED));
    }

}