package com.samsamhajo.deepground.chat.entity;

import com.samsamhajo.deepground.global.BaseDocument;
import jakarta.persistence.Id;
import java.util.List;
import lombok.Getter;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Document(collection = "chat_messages")
@CompoundIndex(def = "{'chat_room_id': 1, 'created_at': -1}")
public class ChatMessage extends BaseDocument {

    @Id
    @Field("chat_message_id")
    private String id;

    @Field("chat_room_id")
    private Long chatRoomId;

    @Field("sender_id")
    private Long senderId;

    @Field("message")
    private String message;

    @Field("media")
    private List<ChatMessageMedia> media;
}
