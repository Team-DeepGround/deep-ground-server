package com.samsamhajo.deepground.notification.entity;

import com.samsamhajo.deepground.global.BaseDocument;
import lombok.Getter;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Document(collection = "notifications")
@CompoundIndex(def = "{'receiver_id': 1, 'created_at': -1}")
@SQLRestriction("is_deleted = false")
public class Notification extends BaseDocument {

    @Id
    private String id;

    @Field("receiver_id")
    private Long receiverId;

    @DBRef
    private final NotificationData data;

    @Field("is_read")
    private boolean read = false;

    @Field("is_deleted")
    private boolean deleted = false;

    private Notification(Long receiverId, NotificationData data) {
        this.receiverId = receiverId;
        this.data = data;
    }

    public static Notification of(Long receiverId, NotificationData data) {
        return new Notification(receiverId, data);
    }

    public void read() {
        this.read = true;
    }

    public void delete() {
       this.deleted = true;
    }
}
