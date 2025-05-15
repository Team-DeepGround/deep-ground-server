package com.samsamhajo.deepground.chat.repository;

import com.samsamhajo.deepground.chat.entity.ChatRoom;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends CrudRepository<ChatRoom, Long> {
}
