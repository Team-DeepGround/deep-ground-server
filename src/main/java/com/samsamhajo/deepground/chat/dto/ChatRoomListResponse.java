package com.samsamhajo.deepground.chat.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomListResponse {

    private List<ChatRoomResponse> chatRooms;
    private int page;
    private boolean hasNext;

    public static ChatRoomListResponse of(
            List<ChatRoomResponse> chatRooms,
            int page,
            boolean hasNext
    ) {
        return ChatRoomListResponse.builder()
                .chatRooms(chatRooms)
                .page(page)
                .hasNext(hasNext)
                .build();
    }
}
