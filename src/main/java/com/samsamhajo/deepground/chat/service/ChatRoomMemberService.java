package com.samsamhajo.deepground.chat.service;

import com.samsamhajo.deepground.chat.entity.ChatMessage;
import com.samsamhajo.deepground.chat.entity.ChatRoom;
import com.samsamhajo.deepground.chat.entity.ChatRoomMember;
import com.samsamhajo.deepground.chat.exception.ChatRoomErrorCode;
import com.samsamhajo.deepground.chat.exception.ChatRoomException;
import com.samsamhajo.deepground.chat.repository.ChatMessageRepository;
import com.samsamhajo.deepground.chat.repository.ChatRoomMemberRepository;
import com.samsamhajo.deepground.member.entity.Member;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatRoomMemberService {

    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public void joinChatRoom(Member member, ChatRoom chatRoom) {
        ChatRoomMember chatRoomMember = ChatRoomMember.of(member, chatRoom);

        // 채팅방에 메시지가 있다면 마지막으로 읽은 메시지 시간을 업데이트
        Optional<ChatMessage> chatMessage = chatMessageRepository.findFirstByChatRoomIdOrderByCreatedAtDesc(
                chatRoom.getId());
        chatMessage.ifPresent(message ->
                chatRoomMember.updateLastReadMessageTime(message.getCreatedAt()));

        chatRoomMemberRepository.save(chatRoomMember);
    }

    @Transactional
    public void leaveChatRoom(Long memberId, Long chatRoomId) {
        ChatRoomMember chatRoomMember =
                chatRoomMemberRepository.findByMemberIdAndChatRoomId(memberId, chatRoomId)
                        .orElseThrow(() -> new ChatRoomException(ChatRoomErrorCode.MEMBER_NOT_FOUND));

        chatRoomMember.softDelete();
    }

    @Transactional(readOnly = true)
    public boolean isChatRoomMember(Long chatRoomId, Long memberId) {
        return chatRoomMemberRepository.existsByChatRoomIdAndMemberId(chatRoomId, memberId);
    }
}
