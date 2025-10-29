package com.samsamhajo.deepground.notification.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Document(collection = "notification_data")
public abstract class NotificationData {

    private static final int CONTENT_LENGTH = 8;

    @Id
    @JsonIgnore
    private String id;

    @Field("type")
    private NotificationType type;

    protected NotificationData() {
        this.type = null;
    }
    protected NotificationData(NotificationType type) {
        this.type = type;
    }

    protected static String truncated(String content) {
        return content.length() > CONTENT_LENGTH
                ? content.substring(0, CONTENT_LENGTH) + "..."
                : content;
    }
}
