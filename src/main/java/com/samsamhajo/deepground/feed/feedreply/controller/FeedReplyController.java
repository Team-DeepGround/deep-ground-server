package com.samsamhajo.deepground.feed.feedreply.controller;

import com.samsamhajo.deepground.feed.feedreply.exception.FeedReplySuccessCode;
import com.samsamhajo.deepground.feed.feedreply.model.FeedReplyCreateRequest;
import com.samsamhajo.deepground.feed.feedreply.model.FeedReplyUpdateRequest;
import com.samsamhajo.deepground.feed.feedreply.model.FetchFeedRepliesResponse;
import com.samsamhajo.deepground.feed.feedreply.service.FeedReplyService;
import com.samsamhajo.deepground.global.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feed/comment/reply")
@RequiredArgsConstructor
public class FeedReplyController {

    private final FeedReplyService feedReplyService;
    private final Long DEV_MEMBER_ID = 1L;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse<?>> createFeedReply(
            @ModelAttribute FeedReplyCreateRequest request) {

        feedReplyService.createFeedReply(request, DEV_MEMBER_ID);

        return ResponseEntity
                .ok(SuccessResponse.of(FeedReplySuccessCode.FEED_REPLY_CREATED));
    }

    @PutMapping(value = "/{feedReplyId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse<?>> updateFeedReply(
            @PathVariable("feedReplyId") Long feedReplyId,
            @ModelAttribute FeedReplyUpdateRequest request) {

        feedReplyService.updateFeedReply(feedReplyId, request);

        return ResponseEntity
                .ok(SuccessResponse.of(FeedReplySuccessCode.FEED_REPLY_UPDATED));
    }

    @DeleteMapping("/{feedReplyId}")
    public ResponseEntity<SuccessResponse<?>> deleteFeedReply(
            @PathVariable("feedReplyId") Long feedReplyId) {

        feedReplyService.deleteFeedReplyId(feedReplyId);

        return ResponseEntity
                .ok(SuccessResponse.of(FeedReplySuccessCode.FEED_REPLY_DELETED));
    }

    @GetMapping("/list/{feedCommentId}")
    public ResponseEntity<SuccessResponse<?>> getFeedReplies(
            @PathVariable("feedCommentId") Long feedCommentId) {

        FetchFeedRepliesResponse response = feedReplyService.getFeedReplies(feedCommentId);

        return ResponseEntity
                .ok(SuccessResponse.of(FeedReplySuccessCode.FEED_REPLY_LIST_FETCHED, response));
    }
} 