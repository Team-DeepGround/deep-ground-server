package com.samsamhajo.deepground.chat.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatMessageRequest {

    private Long senderId; // TODO: @AuthenticationPrincipal 사용 시 senderId 제거
    private String message;
    private List<String> mediaIds;
}
