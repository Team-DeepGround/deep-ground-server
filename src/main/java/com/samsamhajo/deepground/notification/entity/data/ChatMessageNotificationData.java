package com.samsamhajo.deepground.notification.entity.data;

import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
public class ChatMessageNotificationData extends NotificationData {

    @Field("sender_id")
    private Long senderId;

    @Field("sender")
    private String sender;
}
