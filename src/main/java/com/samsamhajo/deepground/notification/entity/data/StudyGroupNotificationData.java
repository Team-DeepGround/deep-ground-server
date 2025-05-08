package com.samsamhajo.deepground.notification.entity.data;

import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
public class StudyGroupNotificationData extends NotificationData {

    @Field("title")
    private String title;

    private StudyGroupNotificationData(Long id, String title) {
        super(id);
        this.title = title;
    }

    public static StudyGroupNotificationData of(Long id, String title) {
        return new StudyGroupNotificationData(id, title);
    }
}
