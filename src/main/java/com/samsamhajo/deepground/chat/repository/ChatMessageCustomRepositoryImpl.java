package com.samsamhajo.deepground.chat.repository;

import com.samsamhajo.deepground.chat.entity.ChatMessage;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@RequiredArgsConstructor
public class ChatMessageCustomRepositoryImpl implements ChatMessageCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public List<ChatMessage> findByChatRoomIdWithCursor(Long chatRoomId, ZonedDateTime cursor, int limit) {
        Criteria criteria = Criteria.where("chat_room_id").is(chatRoomId);
        if (cursor != null) {
            criteria = criteria.and("created_at").lt(cursor);
        }

        Query query = new Query(criteria)
                .with(Sort.by(Direction.DESC, "created_at"))
                .limit(limit + 1);

        return mongoTemplate.find(query, ChatMessage.class);
    }
}
