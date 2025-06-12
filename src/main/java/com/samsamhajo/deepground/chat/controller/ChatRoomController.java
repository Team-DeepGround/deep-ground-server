package com.samsamhajo.deepground.chat.controller;

import com.samsamhajo.deepground.auth.security.CustomUserDetails;
import com.samsamhajo.deepground.chat.dto.ChatRoomMemberInfo;
import com.samsamhajo.deepground.chat.service.ChatRoomMemberService;
import com.samsamhajo.deepground.chat.success.ChatSuccessCode;
import com.samsamhajo.deepground.global.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chatrooms/{chatRoomId}")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomMemberService chatRoomMemberService;

    @GetMapping("/members/{otherMemberId}")
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
}
