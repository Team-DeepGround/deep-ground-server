package com.samsamhajo.deepground.feed.feed.controller;

import com.samsamhajo.deepground.feed.feed.model.FeedMediaResponse;
import com.samsamhajo.deepground.feed.feed.service.FeedMediaService;
import com.samsamhajo.deepground.global.success.SuccessResponse;
import com.samsamhajo.deepground.media.MediaSuccessCode;
import com.samsamhajo.deepground.media.MediaUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/feed/media")
@RequiredArgsConstructor
public class FeedMediaController {

    private final FeedMediaService feedMediaService;


    // 피드 이미지 아이디로 이미지 불러오기
    @GetMapping("/{mediaId}")
    public ResponseEntity<SuccessResponse<FeedMediaResponse>> viewImage(@PathVariable("mediaId") Long mediaId) {
        FeedMediaResponse feedMediaResponse =
                feedMediaService.fetchFeedMedia(mediaId);

        return ResponseEntity.ok()
                .contentType(MediaUtils.getMediaType(feedMediaResponse.getExtension()))
                .body(SuccessResponse.of(MediaSuccessCode.MEDIA_FOUND, feedMediaResponse));
    }
}