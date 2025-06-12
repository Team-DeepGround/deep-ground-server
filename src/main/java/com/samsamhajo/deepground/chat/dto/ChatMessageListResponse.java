package com.samsamhajo.deepground.chat.dto;

import com.samsamhajo.deepground.chat.entity.ChatMessage;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatMessageListResponse {

    private List<ChatMessageResponse> messages;
    private LocalDateTime nextCursor;
    private boolean hasNext;

    public static ChatMessageListResponse of(
            List<ChatMessage> messages,
            LocalDateTime nextCursor,
            boolean hasNext
    ) {
        List<ChatMessageResponse> responses = messages.stream()
                .map(ChatMessageResponse::from)
                .toList();
        return ChatMessageListResponse.builder()
                .messages(responses)
                .nextCursor(nextCursor)
                .hasNext(hasNext)
                .build();
    }
}
