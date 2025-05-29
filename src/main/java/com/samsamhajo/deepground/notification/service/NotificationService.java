package com.samsamhajo.deepground.notification.service;

import com.samsamhajo.deepground.global.message.MessagePublisher;
import com.samsamhajo.deepground.notification.dto.NotificationResponse;
import com.samsamhajo.deepground.notification.entity.Notification;
import com.samsamhajo.deepground.notification.entity.NotificationData;
import com.samsamhajo.deepground.notification.exception.NotificationErrorCode;
import com.samsamhajo.deepground.notification.exception.NotificationException;
import com.samsamhajo.deepground.notification.repository.NotificationDataRepository;
import com.samsamhajo.deepground.notification.repository.NotificationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final MessagePublisher messagePublisher;
    private final NotificationRepository notificationRepository;
    private final NotificationDataRepository notificationDataRepository;

    public void sendNotification(Long receiverId, NotificationData data) {
        sendNotification(List.of(receiverId), data);
    }

    public void sendNotification(List<Long> receiverIds, NotificationData data) {
        NotificationData savedData = notificationDataRepository.save(data);

        List<Notification> notifications = receiverIds.stream()
                .map(receiverId -> Notification.of(receiverId, savedData))
                .toList();

        notificationRepository.saveAll(notifications);

        notifications.forEach(notification -> messagePublisher.convertAndSendToUser(
                String.valueOf(notification.getReceiverId()),
                "/notifications",
                NotificationResponse.from(notification)
        ));
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
}
