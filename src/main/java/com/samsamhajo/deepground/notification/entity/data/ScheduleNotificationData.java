package com.samsamhajo.deepground.notification.entity.data;

import com.samsamhajo.deepground.calendar.entity.StudySchedule;
import com.samsamhajo.deepground.notification.entity.NotificationData;
import com.samsamhajo.deepground.notification.entity.NotificationType;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
public class ScheduleNotificationData extends NotificationData {

    @Field("study_schedule_id")
    private Long studyScheduleId;

    @Field("schedule_title")
    private String title;

    @Field("start_time")
    private LocalDateTime startTime;

    private ScheduleNotificationData(
            NotificationType type,
            Long studyScheduleId,
            String title,
            LocalDateTime startTime
    ) {
        super(type);
        this.studyScheduleId = studyScheduleId;
        this.title = title;
        this.startTime = startTime;
    }

    public static ScheduleNotificationData create(StudySchedule studySchedule) {
        return new ScheduleNotificationData(
                NotificationType.SCHEDULE_CREATE,
                studySchedule.getId(),
                studySchedule.getTitle(),
                studySchedule.getStartTime()
        );
    }

    public static ScheduleNotificationData reminder(StudySchedule studySchedule) {
        return new ScheduleNotificationData(
                NotificationType.SCHEDULE_REMINDER,
                studySchedule.getId(),
                studySchedule.getTitle(),
                studySchedule.getStartTime()
        );
    }
}
