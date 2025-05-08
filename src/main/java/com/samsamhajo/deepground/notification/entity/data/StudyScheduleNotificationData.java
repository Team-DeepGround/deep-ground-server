package com.samsamhajo.deepground.notification.entity.data;

import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
public class StudyScheduleNotificationData extends NotificationData {

    @Field("schedule_title")
    private String title;

    @Field("start_time")
    private LocalDateTime startTime;

    private StudyScheduleNotificationData(Long id, String title, LocalDateTime startTime) {
        super(id);
        this.title = title;
        this.startTime = startTime;
    }

    public static StudyScheduleNotificationData of(Long id, String title, LocalDateTime startTime) {
        return new StudyScheduleNotificationData(id, title, startTime);
    }
}
