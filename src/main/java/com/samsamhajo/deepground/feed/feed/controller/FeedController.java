package com.samsamhajo.deepground.feed.feed.controller;

import com.samsamhajo.deepground.auth.security.CustomUserDetails;
import com.samsamhajo.deepground.feed.feed.entity.Feed;
import com.samsamhajo.deepground.feed.feed.exception.FeedSuccessCode;
import com.samsamhajo.deepground.feed.feed.model.FeedCreateRequest;
import com.samsamhajo.deepground.feed.feed.model.FeedUpdateRequest;
import com.samsamhajo.deepground.feed.feed.model.FetchFeedsResponse;
import com.samsamhajo.deepground.feed.feed.service.FeedService;
import com.samsamhajo.deepground.global.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feed")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse<Feed>> createFeed(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @ModelAttribute FeedCreateRequest request) {

        feedService.createFeed(request, userDetails.getMember());

        return ResponseEntity
                .ok(SuccessResponse.of(FeedSuccessCode.FEED_CREATED));
    }

    @GetMapping("/list")
    public ResponseEntity<SuccessResponse<FetchFeedsResponse>> getFeeds(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Long memberId = userDetails.getMember().getId();
        FetchFeedsResponse response = feedService.getFeeds(pageable, memberId);

        return ResponseEntity
                .ok(SuccessResponse.of(FeedSuccessCode.FEED_LIST_FETCHED, response));
    }
    
    @PutMapping(value = "/{feedId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse<Feed>> updateFeed(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("feedId") Long feedId,
            @ModelAttribute FeedUpdateRequest request) {

        Long memberId = userDetails.getMember().getId();
        feedService.updateFeed(feedId, request, memberId);
        
        return ResponseEntity
                .ok(SuccessResponse.of(FeedSuccessCode.FEED_UPDATED));
    }

    @DeleteMapping("/{feedId}")
    public ResponseEntity<SuccessResponse<Void>> deleteFeed(
            @PathVariable("feedId") Long feedId) {
        feedService.deleteFeed(feedId);

        return ResponseEntity
                .ok(SuccessResponse.of(FeedSuccessCode.FEED_DELETED));
    }
}