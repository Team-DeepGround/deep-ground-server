package com.samsamhajo.deepground.notification.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Document(collection = "notification_data")
public abstract class NotificationData {

    @Id
    @JsonIgnore
    @Field("notification_data_id")
    private String id;

    @Field("type")
    private NotificationType type;

    protected NotificationData(NotificationType type) {
        this.type = type;
    }
}
