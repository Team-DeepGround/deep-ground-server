package com.samsamhajo.deepground.feed.feedreply.model;

import lombok.Getter;
import org.springframework.core.io.InputStreamResource;

@Getter
public class FeedReplyMediaResponse {
    private final InputStreamResource image;
    private final String extension;

    private FeedReplyMediaResponse(InputStreamResource image, String extension) {
        this.image = image;
        this.extension = extension;
    }

    public static FeedReplyMediaResponse of(InputStreamResource image, String extension) {
        return new FeedReplyMediaResponse(image, extension);
    }
} 