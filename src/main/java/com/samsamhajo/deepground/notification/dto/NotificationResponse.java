package com.samsamhajo.deepground.notification.dto;

import com.samsamhajo.deepground.notification.entity.Notification;
import com.samsamhajo.deepground.notification.entity.NotificationData;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationResponse {

    private String id;
    private NotificationData data;
    private boolean read;
    private LocalDateTime createdAt;

    public static NotificationResponse from(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .data(notification.getData())
                .read(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
