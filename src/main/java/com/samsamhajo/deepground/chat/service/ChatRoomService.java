package com.samsamhajo.deepground.chat.service;

import com.samsamhajo.deepground.chat.entity.ChatRoom;
import com.samsamhajo.deepground.chat.entity.ChatRoomMember;
import com.samsamhajo.deepground.chat.entity.ChatRoomType;
import com.samsamhajo.deepground.chat.repository.ChatRoomMemberRepository;
import com.samsamhajo.deepground.chat.repository.ChatRoomRepository;
import com.samsamhajo.deepground.member.entity.Member;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;

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
}
