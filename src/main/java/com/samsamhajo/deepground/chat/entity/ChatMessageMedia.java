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
}
