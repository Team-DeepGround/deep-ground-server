package com.samsamhajo.deepground.chat.service;

import com.samsamhajo.deepground.chat.dto.ChatMediaResponse;
import com.samsamhajo.deepground.chat.dto.ChatMediaUploadResponse;
import com.samsamhajo.deepground.chat.entity.ChatMedia;
import com.samsamhajo.deepground.chat.entity.ChatMessageMedia;
import com.samsamhajo.deepground.chat.exception.ChatErrorCode;
import com.samsamhajo.deepground.chat.exception.ChatException;
import com.samsamhajo.deepground.chat.repository.ChatMediaRepository;
import com.samsamhajo.deepground.media.MediaUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ChatMediaService {

    private final ChatRoomMemberService chatRoomMemberService;
    private final ChatMediaRepository chatMediaRepository;

    @Transactional
    public ChatMediaUploadResponse uploadChatMedia(Long chatRoomId, Long memberId, List<MultipartFile> files) {
        if (!chatRoomMemberService.isChatRoomMember(chatRoomId, memberId)) {
            throw new ChatException(ChatErrorCode.CHATROOM_ACCESS_DENIED);
        }

        List<ChatMedia> chatMedia = chatMediaRepository.saveAll(files.stream()
                .map(file -> {
                    ChatMessageMedia media = ChatMessageMedia.of(
                            MediaUtils.generateMediaUrl(file),
                            file.getOriginalFilename(),
                            file.getSize(),
                            MediaUtils.getExtension(file)
                    );
                    return ChatMedia.of(chatRoomId, memberId, media);
                })
                .toList()
        );

        return ChatMediaUploadResponse.of(chatMedia.stream()
                .map(ChatMedia::getId)
                .toList()
        );
    }

    public ChatMediaResponse fetchMedia(Long chatRoomId, Long memberId, String mediaId) {
        if (!chatRoomMemberService.isChatRoomMember(chatRoomId, memberId)) {
            throw new ChatException(ChatErrorCode.CHATROOM_ACCESS_DENIED);
        }

        ChatMedia chatMedia = chatMediaRepository.getByIdAndChatRoomId(mediaId, chatRoomId)
                .orElseThrow(() -> new ChatException(ChatErrorCode.MEDIA_NOT_FOUND));

        InputStreamResource resource = MediaUtils.getMedia(chatMedia.getMedia().getMediaUrl());
        return ChatMediaResponse.of(
                resource,
                chatMedia.getMedia().getFileName(),
                chatMedia.getMedia().getFileSize(),
                chatMedia.getMedia().getExtension()
        );
    }
}
