package com.samsamhajo.deepground.chat.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.samsamhajo.deepground.chat.entity.ChatRoom;
import com.samsamhajo.deepground.chat.entity.ChatRoomMember;
import com.samsamhajo.deepground.chat.exception.ChatRoomErrorCode;
import com.samsamhajo.deepground.chat.exception.ChatRoomException;
import com.samsamhajo.deepground.chat.repository.ChatRoomMemberRepository;
import com.samsamhajo.deepground.chat.repository.ChatRoomRepository;
import com.samsamhajo.deepground.member.entity.Member;
import java.util.List;
import java.util.Optional;
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

    @Mock
    private ChatRoomMemberRepository chatRoomMemberRepository;

    @Nested
    @DisplayName("채팅방 생성")
    class CreateChatRoom {

        @Test
        @DisplayName("친구 채팅방을 생성한다")
        void createFriendChatRoom() {
            Member member1 = mock(Member.class);
            Member member2 = mock(Member.class);

            ChatRoom chatRoom = chatRoomService.createFriendChatRoom(member1, member2);

            assertThat(chatRoom).isNotNull();
            assertSavedChatRoom(chatRoom);
            assertSavedChatRoomMember(2);
        }

        @Test
        @DisplayName("스터디 그룹 채팅방을 생성한다")
        void createStudyGroupChatRoom() {
            Member member = mock(Member.class);

            ChatRoom chatRoom = chatRoomService.createStudyGroupChatRoom(member);

            assertThat(chatRoom).isNotNull();
            assertSavedChatRoom(chatRoom);
            assertSavedChatRoomMember(1);
        }

        private void assertSavedChatRoom(ChatRoom chatRoom) {
            ArgumentCaptor<ChatRoom> captor = ArgumentCaptor.forClass(ChatRoom.class);
            verify(chatRoomRepository).save(captor.capture());

            ChatRoom savedChatRoom = captor.getValue();
            assertThat(savedChatRoom.getType()).isEqualTo(chatRoom.getType());
        }

        @SuppressWarnings("unchecked")
        private void assertSavedChatRoomMember(int size) {
            ArgumentCaptor<List<ChatRoomMember>> captor = ArgumentCaptor.forClass(List.class);
            verify(chatRoomMemberRepository).saveAll(captor.capture());

            List<ChatRoomMember> savedChatRoomMember = captor.getValue();
            assertThat(savedChatRoomMember).hasSize(size);
        }
    }

    @Nested
    @DisplayName("채팅방 삭제")
    class DeleteChatRoom {

        private final Long chatRoomId = 1L;

        @Test
        @DisplayName("채팅방을 삭제하면 softDelete가 실행된다")
        void deleteChatRoom_softDelete() {

            ChatRoom chatRoom = mock(ChatRoom.class);

            when(chatRoomRepository.findById(chatRoomId))
                    .thenReturn(Optional.of(chatRoom));

            chatRoomService.deleteChatRoom(chatRoomId);

            verify(chatRoomMemberRepository).softDeleteByChatRoomId(chatRoomId);
            verify(chatRoom).softDelete();
        }

        @Test
        @DisplayName("채팅방을 찾을 수 없다면 예외가 발생한다")
        void deleteChatRoom_notFound() {
            when(chatRoomRepository.findById(chatRoomId))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> chatRoomService.deleteChatRoom(chatRoomId))
                    .isInstanceOf(ChatRoomException.class)
                    .hasMessage(ChatRoomErrorCode.ROOM_NOT_FOUND.getMessage());
        }
    }
}
