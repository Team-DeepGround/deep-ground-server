package com.samsamhajo.deepground.notification.entity.data;

import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
public class StudyGroupNotificationData extends NotificationData {

    @Field("title")
    private String title;
}
