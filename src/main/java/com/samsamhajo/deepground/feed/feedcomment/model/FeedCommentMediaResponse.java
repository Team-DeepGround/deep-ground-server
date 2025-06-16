package com.samsamhajo.deepground.feed.feedcomment.model;

import lombok.Getter;
import org.springframework.core.io.InputStreamResource;

@Getter
public class FeedCommentMediaResponse {
    private final InputStreamResource image;
    private final String extension;

    private FeedCommentMediaResponse(InputStreamResource image, String extension) {
        this.image = image;
        this.extension = extension;
    }

    public static FeedCommentMediaResponse of(InputStreamResource image, String extension) {
        return new FeedCommentMediaResponse(image, extension);
    }
}
