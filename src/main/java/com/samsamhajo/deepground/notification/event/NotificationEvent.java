package com.samsamhajo.deepground.notification.event;

import com.samsamhajo.deepground.notification.entity.NotificationData;
import java.util.List;
import lombok.Getter;

@Getter
public class NotificationEvent {

    private final List<Long> receiverIds;
    private final NotificationData data;

    private NotificationEvent(List<Long> receiverIds, NotificationData data) {
        this.receiverIds = receiverIds;
        this.data = data;
    }

    public static NotificationEvent of(Long receiverId, NotificationData data) {
        return new NotificationEvent(List.of(receiverId), data);
    }

    public static NotificationEvent of(List<Long> receiverIds, NotificationData data) {
        return new NotificationEvent(receiverIds, data);
    }
}
