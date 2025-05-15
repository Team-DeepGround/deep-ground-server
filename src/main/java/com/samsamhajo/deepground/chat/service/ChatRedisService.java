package com.samsamhajo.deepground.chat.service;

import com.samsamhajo.deepground.chat.entity.ChatMessage;
import com.samsamhajo.deepground.chat.redis.ChatRedisKeys;
import com.samsamhajo.deepground.chat.repository.ChatMessageRepository;
import com.samsamhajo.deepground.external.redis.RedisKey;
import com.samsamhajo.deepground.global.BaseDocument;
import com.samsamhajo.deepground.external.redis.RedisManager;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRedisService {

    private final RedisManager redisManager;
    private final ChatMessageRepository chatMessageRepository;

    public LocalDateTime getLatestMessageTime(Long chatRoomId) {
        RedisKey key = ChatRedisKeys.getLatestMessageKey(chatRoomId);

        // 채팅방의 가장 최근 메시지를 조회
        Supplier<LocalDateTime> supplier = () -> {
            Optional<ChatMessage> chatMessage =
                    chatMessageRepository.findFirstByChatRoomIdOrderByCreatedAtDesc(chatRoomId);
            return chatMessage.map(BaseDocument::getCreatedAt).orElse(null);
        };

        return redisManager.getAndCache(key, supplier, RedisManager.LONG_TTL);
    }
}
