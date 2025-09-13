package com.samsamhajo.deepground.chat.entity;

import com.samsamhajo.deepground.global.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chat_rooms")
@SQLRestriction("is_deleted = false")
public class ChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "chat_room_type", nullable = false)
    private ChatRoomType type;

    private ChatRoom(ChatRoomType type) {
        this.type = type;
    }

    public static ChatRoom of(ChatRoomType chatRoomType) {
        return new ChatRoom(chatRoomType);
    }
}
