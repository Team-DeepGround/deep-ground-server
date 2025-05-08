package com.samsamhajo.deepground.chat.entity;

import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
public class ChatMessageMedia {

    @Field("media_url")
    private String mediaUrl;

    @Field("file_name")
    private String fileName;

    @Field("file_size")
    private Long fileSize;

    @Field("extension")
    private String extension;

    private ChatMessageMedia(String mediaUrl, String fileName, Long fileSize, String extension) {
        this.mediaUrl = mediaUrl;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.extension = extension;
    }

    public static ChatMessageMedia of(String mediaUrl, String fileName, Long fileSize, String extension) {
        return new ChatMessageMedia(mediaUrl, fileName, fileSize, extension);
    }
}
