package com.samsamhajo.deepground.chat.service;

import com.samsamhajo.deepground.chat.entity.ChatRoom;
import com.samsamhajo.deepground.chat.entity.ChatRoomMember;
import com.samsamhajo.deepground.chat.exception.ChatRoomErrorCode;
import com.samsamhajo.deepground.chat.exception.ChatRoomException;
import com.samsamhajo.deepground.chat.repository.ChatRoomMemberRepository;
import com.samsamhajo.deepground.member.entity.Member;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatRoomMemberService {

    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatRedisService chatRedisService;

    @Transactional
    public void joinChatRoom(Member member, ChatRoom chatRoom) {
        ChatRoomMember chatRoomMember = ChatRoomMember.of(member, chatRoom);

        // 채팅방에 메시지가 있다면 마지막으로 읽은 메시지 시간을 업데이트
        LocalDateTime latestMessageTime = chatRedisService.getLatestMessageTime(chatRoom.getId());
        if (latestMessageTime != null) {
            chatRoomMember.updateLastReadMessageTime(latestMessageTime);
        }

        chatRoomMemberRepository.save(chatRoomMember);
    }

    @Transactional
    public void leaveChatRoom(Long memberId, Long chatRoomId) {
        ChatRoomMember chatRoomMember =
                chatRoomMemberRepository.findByMemberIdAndChatRoomId(memberId, chatRoomId)
                        .orElseThrow(() -> new ChatRoomException(ChatRoomErrorCode.MEMBER_NOT_FOUND));

        chatRoomMember.softDelete();
    }
}
