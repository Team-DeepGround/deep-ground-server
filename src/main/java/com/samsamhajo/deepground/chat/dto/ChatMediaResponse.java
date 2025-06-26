package com.samsamhajo.deepground.chat.dto;

import lombok.Getter;
import org.springframework.core.io.InputStreamResource;

@Getter
public class ChatMediaResponse {
    private final InputStreamResource resource;
    private final String extension;

    private ChatMediaResponse(InputStreamResource resource, String extension) {
        this.resource = resource;
        this.extension = extension;
    }

    public static ChatMediaResponse of(InputStreamResource resource, String extension) {
        return new ChatMediaResponse(resource, extension);
    }
}
