package com.samsamhajo.deepground.chat.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.samsamhajo.deepground.chat.dto.ChatRoomMemberInfo;
import com.samsamhajo.deepground.chat.entity.ChatMessage;
import com.samsamhajo.deepground.chat.entity.ChatRoom;
import com.samsamhajo.deepground.chat.entity.ChatRoomMember;
import com.samsamhajo.deepground.chat.exception.ChatErrorCode;
import com.samsamhajo.deepground.chat.exception.ChatException;
import com.samsamhajo.deepground.chat.repository.ChatMessageRepository;
import com.samsamhajo.deepground.chat.repository.ChatRoomMemberRepository;
import com.samsamhajo.deepground.member.entity.Member;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
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
    private ChatMessageRepository chatMessageRepository;

    private Member member;
    private ChatRoom chatRoom;
    private final Long memberId = 1L;
    private final Long chatRoomId = 1L;
    private final Long otherMemberId = 100L;

    @BeforeEach
    void setUp() {
        member = mock(Member.class);
        chatRoom = mock(ChatRoom.class);
    }

    @Test
    @DisplayName("채팅방에 참여한다")
    void joinChatRoom() {
        // given
        ZonedDateTime createdAt = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        ChatMessage chatMessage = mock(ChatMessage.class);

        when(chatRoom.getId()).thenReturn(chatRoomId);
        when(chatMessage.getCreatedAt()).thenReturn(createdAt);
        when(chatMessageRepository.findFirstByChatRoomIdOrderByCreatedAtDesc(chatRoomId)).thenReturn(
                Optional.of(chatMessage));

        // when
        chatRoomMemberService.joinChatRoom(member, chatRoom);

        // then
        ArgumentCaptor<ChatRoomMember> captor = ArgumentCaptor.forClass(ChatRoomMember.class);
        verify(chatRoomMemberRepository).save(captor.capture());

        ChatRoomMember savedChatRoomMember = captor.getValue();
        assertThat(savedChatRoomMember).isNotNull();
        assertThat(savedChatRoomMember.getLastReadMessageTime()).isEqualTo(createdAt);
    }

    @Nested
    @DisplayName("채팅방 나가기")
    class LeaveChatRoom {

        @Test
        @DisplayName("채팅방을 나가면 softDelete가 실행된다")
        void leaveChatRoom_softDelete() {
            // given
            ChatRoomMember chatRoomMember = mock(ChatRoomMember.class);

            when(chatRoomMemberRepository.findByChatRoomIdAndMemberId(chatRoomId, memberId))
                    .thenReturn(Optional.of(chatRoomMember));

            // when
            chatRoomMemberService.leaveChatRoom(chatRoomId, memberId);

            // then
            verify(chatRoomMember).softDelete();
        }

        @Test
        @DisplayName("채팅방 멤버를 찾을 수 없다면 예외가 발생한다")
        void leaveChatRoom_notFound() {
            // given
            when(chatRoomMemberRepository.findByChatRoomIdAndMemberId(chatRoomId, memberId))
                    .thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> chatRoomMemberService.leaveChatRoom(chatRoomId, memberId))
                    .isInstanceOf(ChatException.class)
                    .hasMessage(ChatErrorCode.CHATROOM_MEMBER_NOT_FOUND.getMessage());
        }
    }

    @Test
    @DisplayName("채팅방 멤버를 조회한다")
    void getChatRoomMemberInfo_success() {
        // given
        ChatRoom chatRoom = mock(ChatRoom.class);
        ZonedDateTime lastReadMessageTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        Member member = Member.createLocalMember("test@test.com", "password", "test");
        ChatRoomMember otherMember = ChatRoomMember.of(member, chatRoom, lastReadMessageTime);

        when(chatRoomMemberRepository.existsByChatRoomIdAndMemberId(chatRoomId, memberId)).thenReturn(true);
        when(chatRoomMemberRepository.findByChatRoomIdAndMemberId(chatRoomId, otherMemberId))
                .thenReturn(Optional.of(otherMember));

        // when
        ChatRoomMemberInfo info = chatRoomMemberService.getChatRoomMemberInfo(chatRoomId, memberId, otherMemberId);

        // then
        verify(chatRoomMemberRepository).findByChatRoomIdAndMemberId(eq(chatRoomId), eq(otherMemberId));
        assertThat(info).isNotNull();
        assertThat(info.getNickname()).isEqualTo(member.getNickname());
        assertThat(info.getLastReadMessageTime()).isEqualTo(otherMember.getLastReadMessageTime());
    }

    @Test
    @DisplayName("채팅방에 접근할 수 없다면 예외가 발생한다")
    void getChatRoomMemberInfo_accessDenied_throwsException() {
        // given
        when(chatRoomMemberRepository.existsByChatRoomIdAndMemberId(chatRoomId, memberId)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> chatRoomMemberService.getChatRoomMemberInfo(chatRoomId, memberId, otherMemberId))
                .isInstanceOf(ChatException.class)
                .hasMessage(ChatErrorCode.CHATROOM_ACCESS_DENIED.getMessage());
    }

    @Test
    @DisplayName("채팅방 멤버를 찾을 수 없다면 예외가 발생한다")
    void getChatRoomMemberInfo_notFound_throwsException() {
        // given
        when(chatRoomMemberRepository.existsByChatRoomIdAndMemberId(chatRoomId, memberId)).thenReturn(true);
        when(chatRoomMemberRepository.findByChatRoomIdAndMemberId(chatRoomId, otherMemberId))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> chatRoomMemberService.getChatRoomMemberInfo(chatRoomId, memberId, otherMemberId))
                .isInstanceOf(ChatException.class)
                .hasMessage(ChatErrorCode.CHATROOM_MEMBER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("채팅방의 모든 멤버를 조회한다")
    void getChatRoomMemberInfos_success() {
        // given
        ChatRoom chatRoom = mock(ChatRoom.class);
        ZonedDateTime lastReadMessageTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        Member member1 = mock(Member.class);
        Member member2 = mock(Member.class);
        Member member3 = mock(Member.class);
        List<ChatRoomMember> members = List.of(
                ChatRoomMember.of(member1, chatRoom, lastReadMessageTime),
                ChatRoomMember.of(member2, chatRoom, lastReadMessageTime),
                ChatRoomMember.of(member3, chatRoom, lastReadMessageTime)
        );

        when(chatRoomMemberRepository.findByChatRoomId(chatRoomId)).thenReturn(members);

        // when
        List<ChatRoomMemberInfo> infos = chatRoomMemberService.getChatRoomMemberInfos(chatRoomId, memberId);

        // then
        verify(chatRoomMemberRepository).findByChatRoomId(eq(chatRoomId));
        assertThat(infos).isNotNull();
        assertThat(infos).hasSize(3);
    }
}
