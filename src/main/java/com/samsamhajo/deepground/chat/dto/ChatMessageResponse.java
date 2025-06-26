package com.samsamhajo.deepground.chat.dto;

import com.samsamhajo.deepground.chat.entity.ChatMessage;
import com.samsamhajo.deepground.chat.entity.ChatMessageMedia;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatMessageResponse {

    private String id;
    private Long senderId;
    private String message;
    private List<ChatMessageMedia> media;
    private List<String> mediaIds;
    private LocalDateTime createdAt;

    public static ChatMessageResponse from(ChatMessage chatMessage) {
        return ChatMessageResponse.builder()
                .id(chatMessage.getId())
                .senderId(chatMessage.getSenderId())
                .message(chatMessage.getMessage())
                .media(Optional.ofNullable(chatMessage.getMedia()).orElse(List.of()))
                .mediaIds(Optional.ofNullable(chatMessage.getMediaIds()).orElse(List.of()))
                .createdAt(chatMessage.getCreatedAt())
                .build();
    }
}
