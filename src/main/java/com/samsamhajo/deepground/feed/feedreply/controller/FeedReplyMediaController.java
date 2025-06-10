package com.samsamhajo.deepground.feed.feedreply.controller;

import com.samsamhajo.deepground.feed.feedcomment.service.FeedCommentMediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feed/reply/media")
public class FeedReplyMediaController {

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
