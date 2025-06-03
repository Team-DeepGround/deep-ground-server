package com.samsamhajo.deepground.feed.feedreply.controller;

import com.samsamhajo.deepground.feed.feedcomment.exception.FeedCommentSuccessCode;
import com.samsamhajo.deepground.feed.feedreply.model.FeedReplyCreateRequest;
import com.samsamhajo.deepground.feed.feedreply.service.FeedReplyService;
import com.samsamhajo.deepground.global.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
                .ok(SuccessResponse.of(FeedCommentSuccessCode.FEED_COMMENT_CREATED));
    }
} 