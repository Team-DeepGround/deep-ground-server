package com.samsamhajo.deepground.chat.controller;

import com.samsamhajo.deepground.auth.security.CustomUserDetails;
import com.samsamhajo.deepground.chat.dto.ChatMessageListResponse;
import com.samsamhajo.deepground.chat.dto.ChatMessageRequest;
import com.samsamhajo.deepground.chat.dto.ChatRoomMemberInfo;
import com.samsamhajo.deepground.chat.dto.ChatRoomInitResponse;
import com.samsamhajo.deepground.chat.dto.ReadMessageRequest;
import com.samsamhajo.deepground.chat.service.ChatMessageService;
import com.samsamhajo.deepground.chat.service.ChatRoomMemberService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

@Controller
@MessageMapping("/chatrooms/{chatRoomId}")
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatRoomMemberService chatRoomMemberService;
    private final ChatMessageService chatMessageService;

    @SubscribeMapping("/init")
    public ChatRoomInitResponse init(
            @DestinationVariable Long chatRoomId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMember().getId();

        List<ChatRoomMemberInfo> memberInfos = chatRoomMemberService.getChatRoomMemberInfos(chatRoomId, memberId);
        ChatMessageListResponse chatMessage = chatMessageService.getMessages(chatRoomId, null, 20);
        return ChatRoomInitResponse.of(memberInfos, chatMessage);
    }

    @MessageMapping("/message")
    public void message(
            @DestinationVariable Long chatRoomId,
            @Payload ChatMessageRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMember().getId();
        chatMessageService.sendMessage(chatRoomId, memberId, request);
    }

    @MessageMapping("/read")
    public void read(
            @DestinationVariable Long chatRoomId,
            @Payload ReadMessageRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMember().getId();
        chatMessageService.readMessage(chatRoomId, memberId, request.getLastReadMessageTime());
    }
}
