package com.samsamhajo.deepground.chat.service;

import com.samsamhajo.deepground.chat.entity.ChatMessage;
import com.samsamhajo.deepground.chat.redis.ChatRedisKeys;
import com.samsamhajo.deepground.chat.repository.ChatMessageRepository;
import com.samsamhajo.deepground.global.BaseDocument;
import com.samsamhajo.deepground.utils.redis.RedisUtil;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRedisService {

    private final RedisUtil redisUtil;
    private final ChatMessageRepository chatMessageRepository;

    public LocalDateTime getLatestMessageTime(Long chatRoomId) {
        String key = ChatRedisKeys.getLatestMessageKey(chatRoomId);
        Supplier<LocalDateTime> supplier = () -> loadLatestMessageTime(chatRoomId);

        return redisUtil.getAndCache(key, supplier, RedisUtil.LONG_TTL);
    }

    private LocalDateTime loadLatestMessageTime(Long chatRoomId) {
        // 채팅방의 가장 최근 메시지를 조회
        Optional<ChatMessage> chatMessage =
                chatMessageRepository.findFirstByChatRoomIdOrderByCreatedAtDesc(chatRoomId);

        return chatMessage.map(BaseDocument::getCreatedAt).orElse(null);
    }
}
