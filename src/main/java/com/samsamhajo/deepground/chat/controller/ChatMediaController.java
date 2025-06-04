package com.samsamhajo.deepground.chat.controller;

import com.samsamhajo.deepground.auth.security.CustomUserDetails;
import com.samsamhajo.deepground.chat.dto.ChatMediaResponse;
import com.samsamhajo.deepground.chat.service.ChatMediaService;
import com.samsamhajo.deepground.chat.success.ChatMediaSuccessCode;
import com.samsamhajo.deepground.global.success.SuccessResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/chatrooms/{chatRoomId}/media")
@RequiredArgsConstructor
public class ChatMediaController {

    private final ChatMediaService chatMediaService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse<ChatMediaResponse>> uploadChatMedia(
            @PathVariable Long chatRoomId,
            @RequestParam List<MultipartFile> files,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMember().getId();

        ChatMediaResponse response = chatMediaService.uploadChatMedia(chatRoomId, memberId, files);
        return ResponseEntity
                .ok(SuccessResponse.of(ChatMediaSuccessCode.CHAT_MEDIA_UPLOADED, response));
    }
}
