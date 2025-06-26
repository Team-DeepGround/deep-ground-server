package com.samsamhajo.deepground.feed.feed.controller;

import com.samsamhajo.deepground.feed.feed.model.FeedMediaResponse;
import com.samsamhajo.deepground.feed.feed.service.FeedMediaService;
import com.samsamhajo.deepground.media.MediaUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feed/media")
@RequiredArgsConstructor
public class FeedMediaController {

    private final FeedMediaService feedMediaService;

    @GetMapping("/{mediaId}")
    public ResponseEntity<InputStreamResource> viewImage(@PathVariable("mediaId") Long mediaId) {
        FeedMediaResponse feedMediaResponse =
                feedMediaService.fetchFeedMedia(mediaId);

        return ResponseEntity.ok()
                .contentType(MediaUtils.getMediaType(feedMediaResponse.getExtension()))
                .body(feedMediaResponse.getImage());
    }
}