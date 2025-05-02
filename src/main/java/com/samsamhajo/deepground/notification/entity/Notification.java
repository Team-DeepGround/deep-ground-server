package com.samsamhajo.deepground.notification.entity;

import com.samsamhajo.deepground.global.BaseDocument;
import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Document(collection = "notifications")
@CompoundIndex(def = "{'receiver_id': 1, 'created_at': -1}")
public class Notification extends BaseDocument {

    @Id
    @Field("notification_id")
    private String id;

    @Field("receiver_id")
    private Long receiverId;

    @DBRef
    @Field("notification_message_id")
    private NotificationMessage notificationMessage;

    @Field("is_read")
    private boolean read = false;

    public void read() {
        this.read = true;
    }
}
