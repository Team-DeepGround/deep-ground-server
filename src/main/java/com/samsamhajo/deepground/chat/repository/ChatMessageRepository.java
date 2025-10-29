package com.samsamhajo.deepground.chat.repository;

import com.samsamhajo.deepground.chat.entity.ChatMessage;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String>, ChatMessageCustomRepository {

    Optional<ChatMessage> findFirstByChatRoomIdOrderByCreatedAtDesc(Long chatRoomId);

    Long countByChatRoomIdAndCreatedAtAfter(Long chatRoomId, ZonedDateTime lastReadMessageTime);
}
