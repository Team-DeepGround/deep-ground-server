package com.samsamhajo.deepground.chat.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.samsamhajo.deepground.chat.dto.ChatMessageListResponse;
import com.samsamhajo.deepground.chat.dto.ChatMessageRequest;
import com.samsamhajo.deepground.chat.dto.ChatMessageResponse;
import com.samsamhajo.deepground.chat.dto.ReadMessageResponse;
import com.samsamhajo.deepground.chat.entity.ChatMedia;
import com.samsamhajo.deepground.chat.entity.ChatMessage;
import com.samsamhajo.deepground.chat.entity.ChatMessageMedia;
import com.samsamhajo.deepground.chat.entity.ChatRoomMember;
import com.samsamhajo.deepground.chat.exception.ChatErrorCode;
import com.samsamhajo.deepground.chat.exception.ChatException;
import com.samsamhajo.deepground.chat.exception.ChatMessageErrorCode;
import com.samsamhajo.deepground.chat.exception.ChatMessageException;
import com.samsamhajo.deepground.chat.repository.ChatMediaRepository;
import com.samsamhajo.deepground.chat.repository.ChatMessageRepository;
import com.samsamhajo.deepground.chat.repository.ChatRoomMemberRepository;
import com.samsamhajo.deepground.global.message.MessagePublisher;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ChatMessageServiceTest {

    @InjectMocks
    private ChatMessageService chatMessageService;

    @Mock
    private MessagePublisher messagePublisher;

    @Mock
    private ChatRoomMemberRepository chatRoomMemberRepository;

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Mock
    private ChatMediaRepository chatMediaRepository;

    private final Long chatRoomId = 1L;
    private final Long memberId = 1L;
    private final LocalDateTime cursor = LocalDateTime.now();
    private final int limit = 20;

    @Test
    @DisplayName("채팅 메시지를 조회하고 다음 페이지가 존재한다")
    void getMessages_hasNextTrue_success() {
        // given
        int limit = 2;
        List<ChatMessage> messages = List.of(
                ChatMessage.of(chatRoomId, memberId, "테스트 1"),
                ChatMessage.of(chatRoomId, memberId, "테스트 2"),
                ChatMessage.of(chatRoomId, memberId, "테스트 3")
        );
        when(chatRoomMemberRepository.existsByChatRoomIdAndMemberId(chatRoomId, memberId)).thenReturn(true);
        when(chatMessageRepository.findByChatRoomIdWithCursor(chatRoomId, cursor, limit)).thenReturn(messages);

        // when
        ChatMessageListResponse response = chatMessageService.getMessages(chatRoomId, memberId, cursor, limit);

        // then
        assertThat(response.getMessages()).hasSize(limit);
        assertThat(response.isHasNext()).isTrue();
    }

    @Test
    @DisplayName("채팅 메시지를 조회하고 다음 페이지가 존재하지 않는다")
    void getMessages_hasNextFalse_success() {
        // given
        List<ChatMessage> messages = List.of(
                ChatMessage.of(chatRoomId, memberId, "테스트 1"),
                ChatMessage.of(chatRoomId, memberId, "테스트 2"),
                ChatMessage.of(chatRoomId, memberId, "테스트 3")
        );
        when(chatRoomMemberRepository.existsByChatRoomIdAndMemberId(chatRoomId, memberId)).thenReturn(true);
        when(chatMessageRepository.findByChatRoomIdWithCursor(chatRoomId, cursor, limit)).thenReturn(messages);

        // when
        ChatMessageListResponse response = chatMessageService.getMessages(chatRoomId, memberId, cursor, limit);

        // then
        assertThat(response.getMessages()).hasSize(3);
        assertThat(response.isHasNext()).isFalse();
    }

    @Test
    @DisplayName("채팅방에 접근할 수 없다면 예외가 발생한다")
    void getMessages_accessDenied_throwsException() {
        // given
        when(chatRoomMemberRepository.existsByChatRoomIdAndMemberId(chatRoomId, memberId)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> chatMessageService.getMessages(chatRoomId, memberId, cursor, limit))
                .isInstanceOf(ChatException.class)
                .hasMessage(ChatErrorCode.CHATROOM_ACCESS_DENIED.getMessage());
    }

    @Test
    @DisplayName("채팅 메시지를 전송한다")
    void sendMessage_success() {
        // given
        String message = "테스트 채팅 메시지";
        ChatMessageRequest request = ChatMessageRequest.builder()
                .message(message)
                .build();
        ChatMessage chatMessage = ChatMessage.of(chatRoomId, memberId, message);
        when(chatMessageRepository.save(any(ChatMessage.class))).thenReturn(chatMessage);

        ChatRoomMember chatRoomMember = mock(ChatRoomMember.class);
        when(chatRoomMemberRepository.findByChatRoomIdAndMemberId(chatRoomId, memberId))
                .thenReturn(Optional.of(chatRoomMember));
        when(chatRoomMember.updateLastReadMessageTime(any())).thenReturn(true);

        // when
        chatMessageService.sendMessage(chatRoomId, memberId, request);

        // then
        ArgumentCaptor<ChatMessage> captor = ArgumentCaptor.forClass(ChatMessage.class);
        verify(chatMessageRepository).save(captor.capture());

        ChatMessage savedChatMessage = captor.getValue();
        assertThat(savedChatMessage.getChatRoomId()).isEqualTo(chatRoomId);
        assertThat(savedChatMessage.getSenderId()).isEqualTo(memberId);
        assertThat(savedChatMessage.getMessage()).isEqualTo(message);

        String expectedDestination = "/chatrooms/" + chatRoomId + "/message";
        verify(messagePublisher).convertAndSend(eq(expectedDestination), any(ChatMessageResponse.class));
    }

    @Test
    @DisplayName("메시지와 미디어가 모두 비어있으면 예외가 발생한다")
    void sendMessage_withEmptyContent_throwsException() {
        // given
        ChatMessageRequest request = ChatMessageRequest.builder()
                .build();

        // when & then
        assertThatThrownBy(() -> chatMessageService.sendMessage(chatRoomId, memberId, request))
                .isInstanceOf(ChatMessageException.class)
                .hasMessage(ChatMessageErrorCode.INVALID_MESSAGE.getMessage());
    }

    @Test
    @DisplayName("미디어와 채팅 메시지를 전송한다")
    void sendMessage_withMedia_success() {
        // given
        List<String> mediaIds = List.of("1234");
        ChatMessageMedia messageMedia = ChatMessageMedia.of("/media/test.jpg", "test.jpg", 0L, "jpg");
        List<ChatMedia> chatMedia = List.of(ChatMedia.of(chatRoomId, memberId, messageMedia));

        String message = "테스트 채팅 메시지";
        ChatMessageRequest request = ChatMessageRequest.builder()
                .message(message)
                .mediaIds(mediaIds)
                .build();
        ChatMessage chatMessage = ChatMessage.of(chatRoomId, memberId, message);
        when(chatMessageRepository.save(any(ChatMessage.class))).thenReturn(chatMessage);
        when(chatMediaRepository.findAllByIdAndStatusPending(mediaIds)).thenReturn(chatMedia);

        ChatRoomMember chatRoomMember = mock(ChatRoomMember.class);
        when(chatRoomMemberRepository.findByChatRoomIdAndMemberId(chatRoomId, memberId))
                .thenReturn(Optional.of(chatRoomMember));
        when(chatRoomMember.updateLastReadMessageTime(any())).thenReturn(true);

        // when
        chatMessageService.sendMessage(chatRoomId, memberId, request);

        // then
        ArgumentCaptor<ChatMessage> captor = ArgumentCaptor.forClass(ChatMessage.class);
        verify(chatMessageRepository).save(captor.capture());

        ChatMessage savedChatMessage = captor.getValue();
        assertThat(savedChatMessage.getChatRoomId()).isEqualTo(chatRoomId);
        assertThat(savedChatMessage.getSenderId()).isEqualTo(memberId);
        assertThat(savedChatMessage.getMessage()).isEqualTo(message);
        assertThat(savedChatMessage.getMedia()).isEqualTo(List.of(messageMedia));

        String expectedDestination = "/chatrooms/" + chatRoomId + "/message";
        verify(messagePublisher).convertAndSend(eq(expectedDestination), any(ChatMessageResponse.class));
    }

    @Test
    @DisplayName("채팅 메시지를 읽음 처리한다")
    void readMessage_success() {
        // given
        LocalDateTime latestMessageTime = LocalDateTime.now();
        ChatRoomMember chatRoomMember = mock(ChatRoomMember.class);

        when(chatRoomMemberRepository.findByChatRoomIdAndMemberId(chatRoomId, memberId))
                .thenReturn(Optional.of(chatRoomMember));
        when(chatRoomMember.updateLastReadMessageTime(eq(latestMessageTime))).thenReturn(true);

        // when
        chatMessageService.readMessage(chatRoomId, memberId, latestMessageTime);

        // then
        verify(chatRoomMemberRepository).findByChatRoomIdAndMemberId(eq(chatRoomId), eq(memberId));
        verify(chatRoomMember).updateLastReadMessageTime(eq(latestMessageTime));

        ArgumentCaptor<ReadMessageResponse> captor = ArgumentCaptor.forClass(ReadMessageResponse.class);
        String expectedDestination = "/chatrooms/" + chatRoomId + "/read-receipt";
        verify(messagePublisher).convertAndSend(eq(expectedDestination), captor.capture());

        ReadMessageResponse sentResponse = captor.getValue();
        assertThat(sentResponse.getMemberId()).isEqualTo(memberId);
        assertThat(sentResponse.getLastReadMessageTime()).isEqualTo(latestMessageTime);
    }

    @Test
    @DisplayName("읽음 처리 시 채팅방 멤버를 찾을 수 없다면 예외가 발생한다")
    void readMessage_notFound_throwsException() {
        // given
        LocalDateTime latestMessageTime = LocalDateTime.now();

        when(chatRoomMemberRepository.findByChatRoomIdAndMemberId(chatRoomId, memberId))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy((() -> chatMessageService.readMessage(chatRoomId, memberId, latestMessageTime)))
                .isInstanceOf(ChatMessageException.class)
                .hasMessage(ChatMessageErrorCode.CHATROOM_MEMBER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("읽음 처리 시 메시지 시간이 유효하지 않으면 예외가 발생한다")
    void readMessage_invalidMessageTime_throwsException() {
        // given
        LocalDateTime latestMessageTime = LocalDateTime.now().plusHours(1);
        ChatRoomMember chatRoomMember = mock(ChatRoomMember.class);

        when(chatRoomMemberRepository.findByChatRoomIdAndMemberId(chatRoomId, memberId))
                .thenReturn(Optional.of(chatRoomMember));
        when(chatRoomMember.updateLastReadMessageTime(eq(latestMessageTime))).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> chatMessageService.readMessage(chatRoomId, memberId, latestMessageTime))
                .isInstanceOf(ChatMessageException.class)
                .hasMessage(ChatMessageErrorCode.INVALID_MESSAGE_TIME.getMessage());
    }
}
