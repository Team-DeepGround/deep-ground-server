package com.samsamhajo.deepground.chat.entity;

import com.samsamhajo.deepground.global.BaseDocument;
import java.util.List;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Document(collection = "chat_messages")
@CompoundIndex(def = "{'chat_room_id': 1, 'created_at': -1}")
public class ChatMessage extends BaseDocument {

    @Id
    private String id;

    @Field("chat_room_id")
    private Long chatRoomId;

    @Field("sender_id")
    private Long senderId;

    @Field("message")
    private String message;

    @Field("media")
    private List<ChatMessageMedia> media;

    @Field("media_id")
    private List<String> mediaIds;

    private ChatMessage(Long chatRoomId, Long senderId, String message, List<ChatMessageMedia> media,
                        List<String> mediaIds) {
        this.chatRoomId = chatRoomId;
        this.senderId = senderId;
        this.message = message;
        this.media = media;
        this.mediaIds = mediaIds;
    }

    public static ChatMessage of(Long chatRoomId, Long senderId, String message) {
        return new ChatMessage(chatRoomId, senderId, message, null, null);
    }

    public static ChatMessage of(Long chatRoomId, Long senderId, String message,
                                 List<ChatMessageMedia> media, List<String> mediaIds) {
        return new ChatMessage(chatRoomId, senderId, message, media, mediaIds);
    }
}
