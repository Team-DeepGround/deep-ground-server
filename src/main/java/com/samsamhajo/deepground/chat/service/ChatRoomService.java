package com.samsamhajo.deepground.chat.service;

import com.samsamhajo.deepground.chat.entity.ChatMessage;
import com.samsamhajo.deepground.chat.entity.ChatRoom;
import com.samsamhajo.deepground.chat.entity.ChatRoomMember;
import com.samsamhajo.deepground.chat.entity.ChatRoomType;
import com.samsamhajo.deepground.chat.repository.ChatMessageRepository;
import com.samsamhajo.deepground.chat.repository.ChatRoomMemberRepository;
import com.samsamhajo.deepground.chat.repository.ChatRoomRepository;
import com.samsamhajo.deepground.member.entity.Member;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public ChatRoom createFriendChatRoom(Member member1, Member member2) {
        ChatRoom chatRoom = createChatRoom(ChatRoomType.FRIEND);
        addInitialMember(chatRoom, member1, member2);
        return chatRoom;
    }

    @Transactional
    public ChatRoom createStudyGroupChatRoom(Member member) {
        ChatRoom chatRoom = createChatRoom(ChatRoomType.STUDY_GROUP);
        addInitialMember(chatRoom, member);
        return chatRoom;
    }

    private ChatRoom createChatRoom(ChatRoomType type) {
        ChatRoom chatRoom = ChatRoom.of(type);
        chatRoomRepository.save(chatRoom);
        return chatRoom;
    }

    private void addInitialMember(ChatRoom chatRoom, Member... members) {
        List<ChatRoomMember> chatRoomMembers = Arrays.stream(members)
                .map(member -> ChatRoomMember.of(member, chatRoom))
                .toList();
        chatRoomMemberRepository.saveAll(chatRoomMembers);
    }

    @Transactional
    public void joinChatRoom(Member member, ChatRoom chatRoom) {
        ChatRoomMember chatRoomMember = ChatRoomMember.of(member, chatRoom);

        // 채팅방의 가장 최근 메시지를 조회
        // TODO: chatrooms:{room_id}:latest_message_id를 조회해서 가져오도록 수정
        Optional<ChatMessage> chatMessage =
                chatMessageRepository.findFirstByChatRoomIdOrderByCreatedAtDesc(chatRoom.getId());

        // 메시지가 있다면 마지막으로 읽은 메시지 시간을 업데이트
        chatMessage.ifPresent(message -> {
            LocalDateTime lastReadMessageTime = message.getCreatedAt();
            chatRoomMember.updateLastReadMessageTime(lastReadMessageTime);
        });

        chatRoomMemberRepository.save(chatRoomMember);
    }
}
