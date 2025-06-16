package com.samsamhajo.deepground.chat.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import com.samsamhajo.deepground.chat.dto.ChatMediaResponse;
import com.samsamhajo.deepground.chat.entity.ChatMedia;
import com.samsamhajo.deepground.chat.entity.ChatMessageMedia;
import com.samsamhajo.deepground.chat.exception.ChatErrorCode;
import com.samsamhajo.deepground.chat.exception.ChatException;
import com.samsamhajo.deepground.chat.repository.ChatMediaRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class ChatMediaServiceTest {

    @InjectMocks
    private ChatMediaService chatMediaService;

    @Mock
    private ChatRoomMemberService chatRoomMemberService;

    @Mock
    private ChatMediaRepository chatMediaRepository;

    private final Long chatRoomId = 1L;
    private final Long memberId = 1L;
    private List<MultipartFile> files;

    @BeforeEach
    void setUp() {
        MultipartFile mockFile = new MockMultipartFile(
                "image",
                "test.jpg",
                "image/jpeg",
                "테스트 이미지 데이터".getBytes()
        );
        files = List.of(mockFile);
    }

    @Test
    @DisplayName("채팅 미디어 업로드에 성공했다면 미디어 ID를 반환한다")
    void uploadChatMedia_success() {
        // given
        ChatMessageMedia messageMedia = ChatMessageMedia.of("/media/test.jpg", "test.jpg", 0L, "jpg");
        ChatMedia media = ChatMedia.of(chatRoomId, memberId, messageMedia);

        when(chatRoomMemberService.isChatRoomMember(chatRoomId, memberId)).thenReturn(true);
        when(chatMediaRepository.saveAll(anyList())).thenReturn(List.of(media));

        // when
        ChatMediaResponse response = chatMediaService.uploadChatMedia(chatRoomId, memberId, files);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getMediaIds()).hasSize(1);
    }

    @Test
    @DisplayName("채팅방 멤버를 찾을 수 없다면 예외가 발생한다")
    void uploadChatMedia_notFound_throwsException() {
        // given
        when(chatRoomMemberService.isChatRoomMember(chatRoomId, memberId)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> chatMediaService.uploadChatMedia(chatRoomId, memberId, files))
                .isInstanceOf(ChatException.class)
                .hasMessage(ChatErrorCode.CHATROOM_MEMBER_NOT_FOUND.getMessage());
    }
}
