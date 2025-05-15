package com.samsamhajo.deepground.chat.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.samsamhajo.deepground.chat.entity.ChatRoom;
import com.samsamhajo.deepground.chat.entity.ChatRoomMember;
import com.samsamhajo.deepground.chat.repository.ChatRoomMemberRepository;
import com.samsamhajo.deepground.member.entity.Member;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChatRoomMemberServiceTest {

    @InjectMocks
    private ChatRoomMemberService chatRoomMemberService;

    @Mock
    private ChatRoomMemberRepository chatRoomMemberRepository;

    @Mock
    private ChatRedisService chatRedisService;

    @Test
    @DisplayName("채팅방에 참여한다")
    void joinChatRoom() {
        Member member = mock(Member.class);
        ChatRoom chatRoom = mock(ChatRoom.class);
        LocalDateTime latestMessageTime = LocalDateTime.now();

        when(chatRoom.getId()).thenReturn(1L);
        when(chatRedisService.getLatestMessageTime(chatRoom.getId())).thenReturn(latestMessageTime);

        chatRoomMemberService.joinChatRoom(member, chatRoom);

        ArgumentCaptor<ChatRoomMember> captor = ArgumentCaptor.forClass(ChatRoomMember.class);
        verify(chatRoomMemberRepository).save(captor.capture());

        ChatRoomMember savedChatRoomMember = captor.getValue();
        assertThat(savedChatRoomMember).isNotNull();
        assertThat(savedChatRoomMember.getLastReadMessageTime()).isEqualTo(latestMessageTime);
    }
}
