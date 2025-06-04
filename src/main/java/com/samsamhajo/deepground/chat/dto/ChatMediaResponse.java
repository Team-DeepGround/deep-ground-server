package com.samsamhajo.deepground.chat.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class ChatMediaResponse {

    private final List<String> mediaIds;

    private ChatMediaResponse(List<String> mediaIds) {
        this.mediaIds = mediaIds;
    }

    public static ChatMediaResponse of(List<String> mediaIds) {
        return new ChatMediaResponse(mediaIds);
    }
}
