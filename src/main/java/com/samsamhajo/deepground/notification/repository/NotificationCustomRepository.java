package com.samsamhajo.deepground.notification.repository;

import com.samsamhajo.deepground.notification.entity.Notification;
import java.time.LocalDateTime;
import java.util.List;

public interface NotificationCustomRepository {

    List<Notification> findByReceiverIdWithCursor(Long receiverId, LocalDateTime cursor, int limit);
}
