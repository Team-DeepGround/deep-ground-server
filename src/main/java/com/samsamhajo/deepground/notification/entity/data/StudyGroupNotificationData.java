package com.samsamhajo.deepground.notification.entity.data;

import com.samsamhajo.deepground.notification.entity.NotificationData;
import com.samsamhajo.deepground.notification.entity.NotificationType;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroup;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
public class StudyGroupNotificationData extends NotificationData {

    @Field("study_group_id")
    private Long studyGroupId;

    @Field("title")
    private String title;

    private StudyGroupNotificationData(NotificationType type, Long studyGroupId, String title) {
        super(type);
        this.studyGroupId = studyGroupId;
        this.title = title;
    }

    public static StudyGroupNotificationData kick(StudyGroup studyGroup) {
        return new StudyGroupNotificationData(
                NotificationType.STUDY_GROUP_KICK,
                studyGroup.getId(),
                studyGroup.getTitle()
        );
    }

    public static StudyGroupNotificationData accept(StudyGroup studyGroup) {
        return new StudyGroupNotificationData(
                NotificationType.STUDY_GROUP_ACCEPT,
                studyGroup.getId(),
                studyGroup.getTitle()
        );
    }

    public static StudyGroupNotificationData join(StudyGroup studyGroup) {
        return new StudyGroupNotificationData(
                NotificationType.STUDY_GROUP_JOIN,
                studyGroup.getId(),
                studyGroup.getTitle()
        );
    }
}
