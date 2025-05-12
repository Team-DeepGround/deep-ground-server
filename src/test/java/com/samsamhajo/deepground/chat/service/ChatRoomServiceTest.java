package com.samsamhajo.deepground.chat.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.samsamhajo.deepground.chat.entity.ChatRoom;
import com.samsamhajo.deepground.chat.entity.ChatRoomMember;
import com.samsamhajo.deepground.chat.repository.ChatRoomMemberRepository;
import com.samsamhajo.deepground.chat.repository.ChatRoomRepository;
import com.samsamhajo.deepground.member.entity.Member;
import java.time.LocalDateTime;
import java.util.List;
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

    @Mock
    private ChatRedisService chatRedisService;

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
    @DisplayName("채팅방 참여")
    class JoinChatRoom {

        @Test
        @DisplayName("채팅방에 참여한다")
        void joinChatRoom() {
            Member member = mock(Member.class);
            ChatRoom chatRoom = mock(ChatRoom.class);
            LocalDateTime latestMessageTime = LocalDateTime.now();

            when(chatRoom.getId()).thenReturn(1L);
            when(chatRedisService.getLatestMessageTime(chatRoom.getId())).thenReturn(latestMessageTime);

            chatRoomService.joinChatRoom(member, chatRoom);

            ArgumentCaptor<ChatRoomMember> captor = ArgumentCaptor.forClass(ChatRoomMember.class);
            verify(chatRoomMemberRepository).save(captor.capture());

            ChatRoomMember savedChatRoomMember = captor.getValue();
            assertThat(savedChatRoomMember).isNotNull();
            assertThat(savedChatRoomMember.getLastReadMessageTime()).isEqualTo(latestMessageTime);
        }
    }
}
