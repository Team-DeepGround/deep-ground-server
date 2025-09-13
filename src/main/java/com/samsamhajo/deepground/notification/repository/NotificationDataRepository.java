package com.samsamhajo.deepground.notification.repository;

import com.samsamhajo.deepground.notification.entity.NotificationData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationDataRepository extends MongoRepository<NotificationData, String> {
}
