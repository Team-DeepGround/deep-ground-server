package com.samsamhajo.deepground.chat.entity;

import com.samsamhajo.deepground.global.BaseDocument;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Document(collection = "chat_media")
public class ChatMedia extends BaseDocument {

    @Id
    private String id;

    @Field("media_url")
    private String mediaUrl;

    @Field("file_name")
    private String fileName;

    @Field("file_size")
    private Long fileSize;

    @Field("extension")
    private String extension;

    @Field("file_status")
    private FileStatus status;

    private ChatMedia(String mediaUrl, String fileName, Long fileSize, String extension) {
        this.mediaUrl = mediaUrl;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.extension = extension;
        this.status = FileStatus.PENDING;
    }

    public static ChatMedia of(String mediaUrl, String fileName, Long fileSize, String extension) {
        return new ChatMedia(mediaUrl, fileName, fileSize, extension);
    }
}
