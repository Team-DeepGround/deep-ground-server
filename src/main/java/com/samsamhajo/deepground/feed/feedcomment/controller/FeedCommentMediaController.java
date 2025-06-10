package com.samsamhajo.deepground.feed.feedcomment.controller;

import com.samsamhajo.deepground.feed.feedcomment.model.FeedCommentMediaResponse;
import com.samsamhajo.deepground.feed.feedcomment.service.FeedCommentMediaService;
import com.samsamhajo.deepground.media.MediaUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feed/comment/media")
public class FeedCommentMediaController {

    private final FeedCommentMediaService feedCommentMediaService;

    @GetMapping("/{mediaId}")
    public ResponseEntity<InputStreamResource> viewImage(@PathVariable("mediaId") Long mediaId) {
        FeedCommentMediaResponse feedCommentMediaResponse =
                feedCommentMediaService.fetchFeedCommentMedia(mediaId);

        return ResponseEntity.ok()
                .contentType(MediaUtils.getMediaType(feedCommentMediaResponse.getExtension()))
                .body(feedCommentMediaResponse.getImage());
    }
}
