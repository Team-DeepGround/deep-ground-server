package com.samsamhajo.deepground.chat.service;

import com.samsamhajo.deepground.chat.entity.ChatMessage;
import com.samsamhajo.deepground.chat.redis.ChatRedisKeys;
import com.samsamhajo.deepground.chat.repository.ChatMessageRepository;
import com.samsamhajo.deepground.chat.repository.ChatRoomMemberRepository;
import com.samsamhajo.deepground.external.redis.RedisKey;
import com.samsamhajo.deepground.global.BaseDocument;
import com.samsamhajo.deepground.external.redis.RedisManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRedisService {

    private final RedisManager redisManager;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;

    public LocalDateTime getLatestMessageTime(Long chatRoomId) {
        RedisKey key = ChatRedisKeys.getLatestMessageKey(chatRoomId);

        Supplier<LocalDateTime> supplier = () -> {
            Optional<ChatMessage> chatMessage =
                    chatMessageRepository.findFirstByChatRoomIdOrderByCreatedAtDesc(chatRoomId);
            return chatMessage.map(BaseDocument::getCreatedAt).orElse(null);
        };

        return redisManager.getValueAndCache(key, supplier, RedisManager.LONG_TTL);
    }

    public void updateLatestMessageTime(Long chatroomId, LocalDateTime latestMessageTime) {
        RedisKey key = ChatRedisKeys.getLatestMessageKey(chatroomId);
        redisManager.setValue(key, latestMessageTime, RedisManager.LONG_TTL);
    }

    public boolean isChatRoomMember(Long memberId, Long chatroomId) {
        RedisKey key = ChatRedisKeys.getChatRoomMemberKey(chatroomId);

        if (redisManager.hasKey(key)) {
            return redisManager.isMember(key, memberId);
        }

        Set<Long> memberIds = getChatRoomMemberIds(chatroomId);
        return memberIds.contains(memberId);
    }

    public Set<Long> getChatRoomMemberIds(Long chatRoomId) {
        RedisKey key = ChatRedisKeys.getChatRoomMemberKey(chatRoomId);

        Supplier<List<Long>> supplier = () ->
                chatRoomMemberRepository.findByChatRoomId(chatRoomId).stream()
                        .map(chatRoomMember -> chatRoomMember.getMember().getId())
                        .toList();

        return redisManager.getSetAndCache(key, supplier, RedisManager.LONG_TTL);
    }
}
