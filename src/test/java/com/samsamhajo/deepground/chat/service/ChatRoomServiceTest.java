package com.samsamhajo.deepground.chat.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import com.samsamhajo.deepground.chat.entity.ChatRoom;
import com.samsamhajo.deepground.chat.repository.ChatRoomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChatRoomServiceTest {

    @InjectMocks
    private ChatRoomService chatRoomService;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Nested
    @DisplayName("채팅방 생성")
    class CreateChatRoom {

        @Test
        @DisplayName("친구 채팅방을 생성한다")
        void createFriendChatRoom() {
            ChatRoom chatRoom = chatRoomService.createFriendChatRoom();
            assertSavedChatRoom(chatRoom);
        }

        @Test
        @DisplayName("스터디 그룹 채팅방을 생성한다")
        void createStudyGroupChatRoom() {
            ChatRoom chatRoom = chatRoomService.createStudyGroupChatRoom();
            assertSavedChatRoom(chatRoom);
        }

        private void assertSavedChatRoom(ChatRoom chatRoom) {
            ArgumentCaptor<ChatRoom> captor = ArgumentCaptor.forClass(ChatRoom.class);
            verify(chatRoomRepository).save(captor.capture());
            ChatRoom savedChatRoom = captor.getValue();
            assertThat(savedChatRoom.getType()).isEqualTo(chatRoom.getType());
        }
    }
}
