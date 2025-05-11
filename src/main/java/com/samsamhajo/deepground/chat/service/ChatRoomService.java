package com.samsamhajo.deepground.chat.service;

import com.samsamhajo.deepground.chat.entity.ChatRoom;
import com.samsamhajo.deepground.chat.entity.ChatRoomType;
import com.samsamhajo.deepground.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public ChatRoom createFriendChatRoom() {
        // TODO: member, friend를 받아 채팅방에 멤버를 추가
        return createChatRoom(ChatRoomType.FRIEND);
    }

    @Transactional
    public ChatRoom createStudyGroupChatRoom() {
        // TODO: member를 받아 채팅방에 멤버를 추가
        return createChatRoom(ChatRoomType.STUDY_GROUP);
    }

    private ChatRoom createChatRoom(ChatRoomType type) {
        ChatRoom chatRoom = ChatRoom.of(type);
        chatRoomRepository.save(chatRoom);
        return chatRoom;
    }
}
