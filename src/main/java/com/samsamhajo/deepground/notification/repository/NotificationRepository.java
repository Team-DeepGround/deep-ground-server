package com.samsamhajo.deepground.notification.repository;

import com.samsamhajo.deepground.notification.entity.Notification;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String>, NotificationCustomRepository {

    Optional<Notification> findByIdAndReceiverId(String id, Long receiverId);

    @Query("{ 'receiver_id': ?0, 'is_read': false }")
    @Update("{ '$set': { 'is_read': true }, '$currentDate': { 'modified_at': true } }")
    void updateAllByReceiverId(Long receiverId);
}
