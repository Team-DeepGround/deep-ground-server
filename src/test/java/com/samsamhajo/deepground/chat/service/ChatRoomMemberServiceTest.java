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
import com.samsamhajo.deepground.member.entity.Member;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

    private Member member;
    private ChatRoom chatRoom;
    private final Long memberId = 1L;
    private final Long chatRoomId = 1L;

    @BeforeEach
    void setUp() {
        member = mock(Member.class);
        chatRoom = mock(ChatRoom.class);
    }

    @Test
    @DisplayName("채팅방에 참여한다")
    void joinChatRoom() {
        LocalDateTime latestMessageTime = LocalDateTime.now();

        when(chatRoom.getId()).thenReturn(chatRoomId);
        when(chatRedisService.getLatestMessageTime(chatRoomId)).thenReturn(latestMessageTime);

        chatRoomMemberService.joinChatRoom(member, chatRoom);

        ArgumentCaptor<ChatRoomMember> captor = ArgumentCaptor.forClass(ChatRoomMember.class);
        verify(chatRoomMemberRepository).save(captor.capture());

        ChatRoomMember savedChatRoomMember = captor.getValue();
        assertThat(savedChatRoomMember).isNotNull();
        assertThat(savedChatRoomMember.getLastReadMessageTime()).isEqualTo(latestMessageTime);
    }

    @Nested
    @DisplayName("채팅방 나가기")
    class LeaveChatRoom {

        @Test
        @DisplayName("채팅방을 나가면 softDelete가 실행된다")
        void leaveChatRoom_softDelete() {
            ChatRoomMember chatRoomMember = ChatRoomMember.of(member, chatRoom);

            when(chatRoomMemberRepository.findByMemberIdAndChatRoomId(memberId, chatRoomId))
                    .thenReturn(Optional.of(chatRoomMember));

            chatRoomMemberService.leaveChatRoom(memberId, chatRoomId);

            assertThat(chatRoomMember.isDeleted()).isTrue();
        }

        @Test
        @DisplayName("스터디 멤버를 찾을 수 없다면 예외가 발생한다")
        void leaveChatRoom_notFound() {
            when(chatRoomMemberRepository.findByMemberIdAndChatRoomId(memberId, chatRoomId))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> chatRoomMemberService.leaveChatRoom(memberId, chatRoomId))
                    .isInstanceOf(ChatRoomException.class)
                    .hasMessage(ChatRoomErrorCode.MEMBER_NOT_FOUND.getMessage());
        }
    }
}
