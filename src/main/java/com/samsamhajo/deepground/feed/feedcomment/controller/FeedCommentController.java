package com.samsamhajo.deepground.feed.feedcomment.controller;

import com.samsamhajo.deepground.auth.security.CustomUserDetails;
import com.samsamhajo.deepground.feed.feedcomment.exception.FeedCommentSuccessCode;
import com.samsamhajo.deepground.feed.feedcomment.model.FeedCommentCreateRequest;
import com.samsamhajo.deepground.feed.feedcomment.model.FeedCommentUpdateRequest;
import com.samsamhajo.deepground.feed.feedcomment.model.FetchFeedCommentsResponse;
import com.samsamhajo.deepground.feed.feedcomment.service.FeedCommentService;
import com.samsamhajo.deepground.global.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feed/comment")
@RequiredArgsConstructor
public class FeedCommentController {

    private final FeedCommentService feedCommentService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse<?>> createFeedComment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @ModelAttribute FeedCommentCreateRequest request) {

        feedCommentService.createFeedComment(request, userDetails.getMember());

        return ResponseEntity
                .ok(SuccessResponse.of(FeedCommentSuccessCode.FEED_COMMENT_CREATED));
    }

    @PutMapping(value = "/{feedCommentId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse<?>> updateFeed(
            @PathVariable("feedCommentId") Long feedCommentId,
            @ModelAttribute FeedCommentUpdateRequest request) {

        feedCommentService.updateFeedComment(feedCommentId, request);

        return ResponseEntity
                .ok(SuccessResponse.of(FeedCommentSuccessCode.FEED_COMMENT_UPDATED));
    }

    @DeleteMapping("/{feedCommentId}")
    public ResponseEntity<SuccessResponse<?>> deleteFeedComment(
            @PathVariable("feedCommentId") Long feedCommentId) {

        feedCommentService.deleteFeedCommentId(feedCommentId);

        return ResponseEntity
                .ok(SuccessResponse.of(FeedCommentSuccessCode.FEED_COMMENT_DELETED));
    }

    @GetMapping("/list/{feedId}")
    public ResponseEntity<SuccessResponse<FetchFeedCommentsResponse>> getFeedComments(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("feedId") Long feedId) {

        FetchFeedCommentsResponse response =
                feedCommentService.getFeedComments(feedId, userDetails.getMember().getId());

        return ResponseEntity
                .ok(SuccessResponse.of(FeedCommentSuccessCode.FEED_COMMENT_LIST_FETCHED, response));
    }
} 