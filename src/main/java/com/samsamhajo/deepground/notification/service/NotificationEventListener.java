package com.samsamhajo.deepground.notification.service;

import com.samsamhajo.deepground.notification.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final NotificationService notificationService;

    @EventListener
    public void handleNotificationEvent(NotificationEvent notificationEvent) {
        notificationService.sendNotification(
                notificationEvent.getReceiverIds(), notificationEvent.getData());
    }
}
