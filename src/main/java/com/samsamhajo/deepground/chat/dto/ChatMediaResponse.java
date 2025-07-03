package com.samsamhajo.deepground.chat.dto;

import lombok.Getter;
import org.springframework.core.io.InputStreamResource;

@Getter
public class ChatMediaResponse {
    private final InputStreamResource resource;
    private final String fileName;
    private final Long fileSize;
    private final String extension;

    private ChatMediaResponse(InputStreamResource resource, String fileName, Long fileSize, String extension) {
        this.resource = resource;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.extension = extension;
    }

    public static ChatMediaResponse of(InputStreamResource resource, String fileName, Long fileSize,
                                       String extension) {
        return new ChatMediaResponse(resource, fileName, fileSize, extension);
    }
}
