package com.samsamhajo.deepground.chat.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomMemberInfo {

    private Long memberId;
    private String nickname;
    private LocalDateTime lastReadMessageTime;
}
