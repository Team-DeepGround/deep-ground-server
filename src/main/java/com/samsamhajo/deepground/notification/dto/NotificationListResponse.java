package com.samsamhajo.deepground.notification.dto;

import com.samsamhajo.deepground.notification.entity.Notification;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationListResponse {

    private List<NotificationResponse> notifications;
    private ZonedDateTime nextCursor;
    private boolean hasNext;

    public static NotificationListResponse of(
            List<Notification> notifications,
            ZonedDateTime nextCursor,
            boolean hasNext
    ) {
        List<NotificationResponse> responses = notifications.stream()
                .map(NotificationResponse::from)
                .toList();
        return NotificationListResponse.builder()
                .notifications(responses)
                .nextCursor(nextCursor)
                .hasNext(hasNext)
                .build();
    }
}
