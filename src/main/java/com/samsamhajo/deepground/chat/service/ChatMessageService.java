package com.samsamhajo.deepground.chat.service;

import com.samsamhajo.deepground.chat.dto.ChatMessageRequest;
import com.samsamhajo.deepground.chat.dto.ChatMessageResponse;
import com.samsamhajo.deepground.chat.entity.ChatMessage;
import com.samsamhajo.deepground.chat.exception.ChatMessageErrorCode;
import com.samsamhajo.deepground.chat.exception.ChatMessageException;
import com.samsamhajo.deepground.chat.repository.ChatMessageRepository;
import com.samsamhajo.deepground.global.message.MessagePublisher;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final MessagePublisher messagePublisher;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRedisService chatRedisService;

    public void sendMessage(Long chatRoomId, Long memberId, ChatMessageRequest request) {
        if (!StringUtils.hasText(request.getMessage()) && CollectionUtils.isEmpty(request.getMediaIds())) {
            throw new ChatMessageException(ChatMessageErrorCode.INVALID_MESSAGE);
        }

        // TODO: 채팅 미디어 관련 로직

        ChatMessage chatMessage = ChatMessage.of(chatRoomId, memberId, request.getMessage());
        chatMessageRepository.save(chatMessage);

        // 가장 최근 메시지 시간 업데이트
        LocalDateTime latestMessageTime = chatMessage.getCreatedAt();
        chatRedisService.updateLatestMessageTime(chatRoomId, latestMessageTime);

        // 채팅 메시지 전송
        String destination = "/chatrooms/" + chatRoomId + "/message";
        messagePublisher.convertAndSend(destination, ChatMessageResponse.from(chatMessage));

        // TODO: 읽지 않은 메시지 개수 로직
    }
}
