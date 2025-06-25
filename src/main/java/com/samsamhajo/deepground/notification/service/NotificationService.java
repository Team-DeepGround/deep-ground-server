package com.samsamhajo.deepground.notification.service;

import com.samsamhajo.deepground.notification.dto.NotificationListResponse;
import com.samsamhajo.deepground.notification.dto.NotificationResponse;
import com.samsamhajo.deepground.notification.dto.NotificationUnreadResponse;
import com.samsamhajo.deepground.notification.entity.Notification;
import com.samsamhajo.deepground.notification.entity.NotificationData;
import com.samsamhajo.deepground.notification.exception.NotificationErrorCode;
import com.samsamhajo.deepground.notification.exception.NotificationException;
import com.samsamhajo.deepground.notification.repository.NotificationDataRepository;
import com.samsamhajo.deepground.notification.repository.NotificationRepository;
import com.samsamhajo.deepground.sse.dto.SseEvent;
import com.samsamhajo.deepground.sse.dto.SseEventType;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationDataRepository notificationDataRepository;
    private final ApplicationEventPublisher eventPublisher;

    public void sendNotification(Long receiverId, NotificationData data) {
        sendNotification(List.of(receiverId), data);
    }

    public void sendNotification(List<Long> receiverIds, NotificationData data) {
        NotificationData savedData = notificationDataRepository.save(data);

        List<Notification> notifications = receiverIds.stream()
                .map(receiverId -> Notification.of(receiverId, savedData))
                .toList();

        notificationRepository.saveAll(notifications);

        notifications.forEach(notification -> {
            NotificationResponse response = NotificationResponse.from(notification);
            SseEvent event = SseEvent.of(notification.getReceiverId(), SseEventType.NOTIFICATION, response);
            eventPublisher.publishEvent(event);
        });
    }

    public NotificationListResponse getNotifications(Long receiverId, LocalDateTime cursor, int limit) {
        List<Notification> notifications = notificationRepository.findByReceiverIdWithCursor(receiverId, cursor, limit);

        boolean hasNext = notifications.size() > limit;
        if (hasNext) {
            notifications = notifications.subList(0, limit);
        }

        LocalDateTime nextCursor = notifications.isEmpty() ? null
                : notifications.get(notifications.size() - 1).getCreatedAt();

        return NotificationListResponse.of(notifications, nextCursor, hasNext);
    }

    public void readNotification(String notificationId, Long receiverId) {
        Notification notification = notificationRepository.findByIdAndReceiverId(notificationId, receiverId)
                .orElseThrow(() -> new NotificationException(NotificationErrorCode.NOTIFICATION_NOT_FOUND));

        if (notification.isRead()) {
            throw new NotificationException(NotificationErrorCode.NOTIFICATION_ALREADY_READ);
        }

        notification.read();
        notificationRepository.save(notification);
    }

    public void readAllNotifications(Long receiverId) {
        notificationRepository.updateAllByReceiverId(receiverId);
    }

    public NotificationUnreadResponse getUnreadCount(Long memberId) {
        Long unreadCount = notificationRepository.countByReceiverIdAndReadFalse(memberId);

        return NotificationUnreadResponse.of(unreadCount);
    }
}
