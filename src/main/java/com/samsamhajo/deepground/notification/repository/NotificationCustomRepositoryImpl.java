package com.samsamhajo.deepground.notification.repository;

import com.samsamhajo.deepground.notification.entity.Notification;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@RequiredArgsConstructor
public class NotificationCustomRepositoryImpl implements NotificationCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public List<Notification> findByReceiverIdWithCursor(Long receiverId, LocalDateTime cursor, int limit) {
        Criteria criteria = Criteria.where("receiver_id").is(receiverId);
        if (cursor != null) {
            criteria = criteria.and("created_at").lt(cursor);
        }

        Query query = new Query(criteria)
                .with(Sort.by(Direction.DESC, "created_at"))
                .limit(limit + 1);

        return mongoTemplate.find(query, Notification.class);
    }
}
