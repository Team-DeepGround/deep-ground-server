package com.samsamhajo.deepground.chat.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class ChatMediaUploadResponse {

    private final List<String> mediaIds;

    private ChatMediaUploadResponse(List<String> mediaIds) {
        this.mediaIds = mediaIds;
    }

    public static ChatMediaUploadResponse of(List<String> mediaIds) {
        return new ChatMediaUploadResponse(mediaIds);
    }
}
