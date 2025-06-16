package com.samsamhajo.deepground.feed.feedreply.controller;

import com.samsamhajo.deepground.feed.feedreply.model.FeedReplyMediaResponse;
import com.samsamhajo.deepground.feed.feedreply.service.FeedReplyMediaService;
import com.samsamhajo.deepground.media.MediaUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feed/reply/media")
public class FeedReplyMediaController {

    private final FeedReplyMediaService feedReplyMediaService;

    @GetMapping("/{mediaId}")
    public ResponseEntity<InputStreamResource> viewImage(@PathVariable Long mediaId) {
        FeedReplyMediaResponse response = feedReplyMediaService.fetchFeedReplyMedia(mediaId);
        
        return ResponseEntity.ok()
                .contentType(MediaUtils.getMediaType(response.getExtension()))
                .body(response.getImage());
    }
}
