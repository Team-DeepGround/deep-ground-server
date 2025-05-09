package com.samsamhajo.deepground.notification.entity.data;

import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
public abstract class NotificationData {

    @Field("id")
    private Long id;

    protected NotificationData(Long id) {
        this.id = id;
    }
}
