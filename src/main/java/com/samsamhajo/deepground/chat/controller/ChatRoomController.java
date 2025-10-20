package com.samsamhajo.deepground.chat.controller;

import com.samsamhajo.deepground.auth.security.CustomUserDetails;
import com.samsamhajo.deepground.chat.dto.ChatMessageListRequest;
import com.samsamhajo.deepground.chat.dto.ChatMessageListResponse;
import com.samsamhajo.deepground.chat.dto.ChatRoomListResponse;
import com.samsamhajo.deepground.chat.dto.ChatRoomMemberInfo;
import com.samsamhajo.deepground.chat.entity.ChatRoomType;
import com.samsamhajo.deepground.chat.service.ChatMessageService;
import com.samsamhajo.deepground.chat.service.ChatRoomMemberService;
import com.samsamhajo.deepground.chat.success.ChatSuccessCode;
import com.samsamhajo.deepground.global.success.SuccessResponse;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chatrooms")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomMemberService chatRoomMemberService;
    private final ChatMessageService chatMessageService;

    @GetMapping
    public ResponseEntity<SuccessResponse<ChatRoomListResponse>> getChatRooms(
            @RequestParam(defaultValue = "FRIEND") ChatRoomType type,
            Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMember().getId();

        ChatRoomListResponse response = chatRoomMemberService.getChatrooms(memberId, type, pageable);
        return ResponseEntity
                .ok(SuccessResponse.of(ChatSuccessCode.CHATROOM_RETRIEVED, response));
    }

    @GetMapping("/{chatRoomId}/members/{otherMemberId}")
    public ResponseEntity<SuccessResponse<ChatRoomMemberInfo>> getChatRoomMemberInfo(
            @PathVariable Long chatRoomId,
            @PathVariable Long otherMemberId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMember().getId();

        ChatRoomMemberInfo info = chatRoomMemberService.getChatRoomMemberInfo(chatRoomId, memberId, otherMemberId);
        return ResponseEntity
                .ok(SuccessResponse.of(ChatSuccessCode.CHATROOM_MEMBER_INFO_RETRIEVED, info));
    }

    @GetMapping("/{chatRoomId}/messages")
    public ResponseEntity<SuccessResponse<ChatMessageListResponse>> getMessages(
            @PathVariable Long chatRoomId,
            @Valid @ModelAttribute ChatMessageListRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMember().getId();

        ChatMessageListResponse messages = chatMessageService.getMessages(
                chatRoomId,
                memberId,
                request.getCursor(),
                request.getLimit()
        );
        return ResponseEntity
                .ok(SuccessResponse.of(ChatSuccessCode.CHAT_MESSAGE_RETRIEVED, messages));
    }

    @DeleteMapping("/{chatroomId}")
    public ResponseEntity<SuccessResponse> leaveFriendChatRoom(
            @PathVariable Long chatroomId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        chatRoomMemberService.leaveChatRoom(chatroomId,userDetails.getMember().getId());

        return ResponseEntity
                .ok(SuccessResponse.of(ChatSuccessCode.SUCCESS_CHATROOM_LEAVE,chatroomId));
    }
}
