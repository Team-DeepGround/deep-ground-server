package com.samsamhajo.deepground.notification.dto;

import lombok.Getter;

@Getter
public class NotificationUnreadResponse {

    private final Long unreadCount;

    private NotificationUnreadResponse(Long unreadCount) {
        this.unreadCount = unreadCount;
    }

    public static NotificationUnreadResponse of(Long unreadCount) {
        return new NotificationUnreadResponse(unreadCount);
    }
}
