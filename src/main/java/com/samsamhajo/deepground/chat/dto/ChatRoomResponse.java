package com.samsamhajo.deepground.chat.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class ChatRoomResponse {

    List<ChatRoomMemberInfo> memberInfos;
    ChatMessageListResponse chatMessage;

    private ChatRoomResponse(List<ChatRoomMemberInfo> memberInfos, ChatMessageListResponse chatMessage) {
        this.memberInfos = memberInfos;
        this.chatMessage = chatMessage;
    }

    public static ChatRoomResponse of(List<ChatRoomMemberInfo> memberInfos, ChatMessageListResponse chatMessage) {
        return new ChatRoomResponse(memberInfos, chatMessage);
    }
}
