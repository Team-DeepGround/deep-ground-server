package com.samsamhajo.deepground.chat.controller;

import com.samsamhajo.deepground.auth.security.CustomUserDetails;
import com.samsamhajo.deepground.chat.dto.ChatMediaResponse;
import com.samsamhajo.deepground.chat.dto.ChatMediaUploadResponse;
import com.samsamhajo.deepground.chat.service.ChatMediaService;
import com.samsamhajo.deepground.chat.success.ChatSuccessCode;
import com.samsamhajo.deepground.global.success.SuccessResponse;
import com.samsamhajo.deepground.media.MediaUtils;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<SuccessResponse<ChatMediaUploadResponse>> uploadChatMedia(
            @PathVariable Long chatRoomId,
            @RequestParam List<MultipartFile> files,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMember().getId();

        ChatMediaUploadResponse response = chatMediaService.uploadChatMedia(chatRoomId, memberId, files);
        return ResponseEntity
                .ok(SuccessResponse.of(ChatSuccessCode.CHAT_MEDIA_UPLOADED, response));
    }

    @GetMapping("/{mediaId}")
    public ResponseEntity<InputStreamResource> getMedia(
            @PathVariable Long chatRoomId,
            @PathVariable String mediaId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMember().getId();

        ChatMediaResponse response = chatMediaService.fetchMedia(chatRoomId, memberId, mediaId);
        String fileName = URLEncoder.encode(response.getFileName(), StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");

        return ResponseEntity.ok()
                .contentLength(response.getFileSize())
                .contentType(MediaUtils.getMediaType(response.getExtension()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileName + "\"; filename*=UTF-8''" + fileName)
                .body(response.getResource());
    }
}
