package com.samsamhajo.deepground.chat.service;

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
import java.util.List;
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
        // 채팅방에 메시지가 있다면 마지막으로 읽은 메시지 시간을 업데이트
        Optional<ChatMessage> chatMessage = chatMessageRepository.findFirstByChatRoomIdOrderByCreatedAtDesc(
                chatRoom.getId());

        ChatRoomMember chatRoomMember = chatMessage
                .map(m -> ChatRoomMember.of(member, chatRoom, m.getCreatedAt()))
                .orElseGet(() -> ChatRoomMember.of(member, chatRoom, LocalDateTime.now()));

        chatRoomMemberRepository.save(chatRoomMember);
    }

    @Transactional
    public void leaveChatRoom(Long chatRoomId, Long memberId) {
        ChatRoomMember chatRoomMember = chatRoomMemberRepository.findByChatRoomIdAndMemberId(chatRoomId, memberId)
                .orElseThrow(() -> new ChatException(ChatErrorCode.CHATROOM_MEMBER_NOT_FOUND));

        chatRoomMember.softDelete();
    }

    @Transactional(readOnly = true)
    public boolean isChatRoomMember(Long chatRoomId, Long memberId) {
        return chatRoomMemberRepository.existsByChatRoomIdAndMemberId(chatRoomId, memberId);
    }

    @Transactional(readOnly = true)
    public ChatRoomMemberInfo getChatRoomMemberInfo(Long chatRoomId, Long memberId, Long otherMemberId) {
        if (!isChatRoomMember(chatRoomId, memberId)) {
            throw new ChatException(ChatErrorCode.CHATROOM_ACCESS_DENIED);
        }

        ChatRoomMember chatRoomMember = chatRoomMemberRepository.findByChatRoomIdAndMemberId(chatRoomId, otherMemberId)
                .orElseThrow(() -> new ChatException(ChatErrorCode.CHATROOM_MEMBER_NOT_FOUND));

        return ChatRoomMemberInfo.from(chatRoomMember);
    }

    @Transactional(readOnly = true)
    public List<ChatRoomMemberInfo> getChatRoomMemberInfos(Long chatRoomId) {
        return chatRoomMemberRepository.findByChatRoomId(chatRoomId).stream()
                .map(ChatRoomMemberInfo::from)
                .toList();
    }
}
