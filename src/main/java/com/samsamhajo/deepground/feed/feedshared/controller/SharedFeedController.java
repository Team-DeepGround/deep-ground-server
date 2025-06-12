package com.samsamhajo.deepground.feed.feedshared.controller;

import com.samsamhajo.deepground.feed.feedshared.dto.SharedFeedRequest;
import com.samsamhajo.deepground.feed.feedshared.exception.SharedFeedSuccessCode;
import com.samsamhajo.deepground.feed.feedshared.service.SharedFeedService;
import com.samsamhajo.deepground.global.success.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feed/share")
public class SharedFeedController {

    private final SharedFeedService sharedFeedService;
    private final Long DEV_MEMBER_ID = 1L;
    @PostMapping
    public ResponseEntity<SuccessResponse<?>> createSharedFeed(
            @Valid @RequestBody SharedFeedRequest request) {

        sharedFeedService.createSharedFeed(request, DEV_MEMBER_ID);
        
        return ResponseEntity.ok(SuccessResponse.of(SharedFeedSuccessCode.FEED_SHARED));
    }
} 