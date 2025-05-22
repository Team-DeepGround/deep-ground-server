package com.samsamhajo.deepground.notification.entity.data;

import com.samsamhajo.deepground.chat.entity.ChatMessage;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.notification.entity.NotificationData;
import com.samsamhajo.deepground.notification.entity.NotificationType;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
public class ChatMessageNotificationData extends NotificationData {

    @Field("chat_room_id")
    private Long chatRoomId;

    @Field("sender_id")
    private Long senderId;

    @Field("sender")
    private String sender;

    private ChatMessageNotificationData(NotificationType type, Long chatRoomId, Long senderId, String sender) {
        super(type);
        this.chatRoomId = chatRoomId;
        this.senderId = senderId;
        this.sender = sender;
    }

    public static ChatMessageNotificationData newMessage(ChatMessage chatMessage, Member sender) {
        return new ChatMessageNotificationData(
                NotificationType.NEW_MESSAGE,
                chatMessage.getChatRoomId(),
                chatMessage.getSenderId(),
                sender.getNickname()
        );
    }
}
