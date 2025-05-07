package com.samsamhajo.deepground.notification.entity.data;

import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
public class ChatMessageNotificationData extends NotificationData {

    @Field("sender_id")
    private Long senderId;

    @Field("sender")
    private String sender;

    private ChatMessageNotificationData(Long id, Long senderId, String sender) {
        super(id);
        this.senderId = senderId;
        this.sender = sender;
    }

    public static ChatMessageNotificationData of(Long id, Long senderId, String sender) {
        return new ChatMessageNotificationData(id, senderId, sender);
    }
}
