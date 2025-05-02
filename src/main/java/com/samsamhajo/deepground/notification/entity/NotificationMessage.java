package com.samsamhajo.deepground.notification.entity;

import com.samsamhajo.deepground.notification.entity.data.NotificationData;
import jakarta.persistence.Id;
import lombok.Getter;
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
}
