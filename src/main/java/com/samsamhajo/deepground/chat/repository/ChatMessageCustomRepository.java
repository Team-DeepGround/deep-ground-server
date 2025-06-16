package com.samsamhajo.deepground.chat.repository;

import com.samsamhajo.deepground.chat.entity.ChatMessage;
import java.time.LocalDateTime;
import java.util.List;

public interface ChatMessageCustomRepository {

    List<ChatMessage> findByChatRoomIdWithCursor(Long chatRoomId, LocalDateTime cursor, int limit);
}
