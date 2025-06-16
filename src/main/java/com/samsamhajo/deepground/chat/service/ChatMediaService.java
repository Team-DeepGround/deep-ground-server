package com.samsamhajo.deepground.chat.service;

import com.samsamhajo.deepground.chat.dto.ChatMediaResponse;
import com.samsamhajo.deepground.chat.entity.ChatMedia;
import com.samsamhajo.deepground.chat.entity.ChatMessageMedia;
import com.samsamhajo.deepground.chat.exception.ChatErrorCode;
import com.samsamhajo.deepground.chat.exception.ChatException;
import com.samsamhajo.deepground.chat.repository.ChatMediaRepository;
import com.samsamhajo.deepground.media.MediaUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ChatMediaService {

    private final ChatRoomMemberService chatRoomMemberService;
    private final ChatMediaRepository chatMediaRepository;

    @Transactional
    public ChatMediaResponse uploadChatMedia(Long chatRoomId, Long memberId, List<MultipartFile> files) {
        if (!chatRoomMemberService.isChatRoomMember(chatRoomId, memberId)) {
            throw new ChatException(ChatErrorCode.CHATROOM_MEMBER_NOT_FOUND);
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

        return ChatMediaResponse.of(chatMedia.stream()
                .map(ChatMedia::getId)
                .toList()
        );
    }
}
