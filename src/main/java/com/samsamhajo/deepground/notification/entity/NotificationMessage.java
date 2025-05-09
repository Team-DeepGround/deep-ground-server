package com.samsamhajo.deepground.notification.entity;

import com.samsamhajo.deepground.notification.entity.data.NotificationData;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Document(collection = "notification_messages")
public class NotificationMessage {

    @Id
    @Field("notification_message_id")
    private String id;

    @Field("notification_type")
    private NotificationType type;

    @Field("notification_data")
    private NotificationData data;

    private NotificationMessage(NotificationType type, NotificationData data) {
        this.type = type;
        this.data = data;
    }

    public static NotificationMessage of(NotificationType type, NotificationData data) {
        return new NotificationMessage(type, data);
    }
}
