package com.samsamhajo.deepground.chat.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class ChatRoomInitResponse {

    List<ChatRoomMemberInfo> memberInfos;
    ChatMessageListResponse chatMessage;

    private ChatRoomInitResponse(List<ChatRoomMemberInfo> memberInfos, ChatMessageListResponse chatMessage) {
        this.memberInfos = memberInfos;
        this.chatMessage = chatMessage;
    }

    public static ChatRoomInitResponse of(List<ChatRoomMemberInfo> memberInfos, ChatMessageListResponse chatMessage) {
        return new ChatRoomInitResponse(memberInfos, chatMessage);
    }
}
