package com.samsamhajo.deepground.feed.feedcomment.controller;

import com.samsamhajo.deepground.feed.feedcomment.service.FeedCommentMediaService;
import com.samsamhajo.deepground.media.MediaErrorCode;
import com.samsamhajo.deepground.media.MediaException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feed-comments/media")
public class FeedCommentMediaController {

    private final FeedCommentMediaService feedCommentMediaService;

    @GetMapping("/{mediaId}")
    public ResponseEntity<InputStreamResource> getMedia(@PathVariable Long mediaId) {
            InputStreamResource media = feedCommentMediaService.getMediaById(mediaId);
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                    .contentType(MediaType.IMAGE_JPEG) // 기본값으로 JPEG 설정
                    .body(media);
    }
}
