package com.samsamhajo.deepground.chat.entity;

import com.samsamhajo.deepground.global.BaseEntity;
import com.samsamhajo.deepground.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chat_room_members")
@SQLRestriction("is_deleted = false")
public class ChatRoomMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @Column(name = "last_read_message_time")
    private ZonedDateTime lastReadMessageTime;

    private ChatRoomMember(Member member, ChatRoom chatRoom, ZonedDateTime lastReadMessageTime) {
        this.member = member;
        this.chatRoom = chatRoom;
        this.lastReadMessageTime = lastReadMessageTime;
    }

    public static ChatRoomMember of(Member member, ChatRoom chatRoom, ZonedDateTime lastReadMessageTime) {
        return new ChatRoomMember(member, chatRoom, lastReadMessageTime);
    }

    public boolean updateLastReadMessageTime(ZonedDateTime lastReadMessageTime) {
        if (lastReadMessageTime == null || lastReadMessageTime.isAfter(ZonedDateTime.now(ZoneId.of("Asia/Seoul")))) {
            return false;
        }

        if (this.lastReadMessageTime.isBefore(lastReadMessageTime)) {
            this.lastReadMessageTime = lastReadMessageTime;
            return true;
        }
        return false;
    }
}
