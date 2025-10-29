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
    private LocalDateTime lastReadMessageTime;

    private ChatRoomMember(Member member, ChatRoom chatRoom, LocalDateTime lastReadMessageTime) {
        this.member = member;
        this.chatRoom = chatRoom;
        this.lastReadMessageTime = lastReadMessageTime;
    }

    public static ChatRoomMember of(Member member, ChatRoom chatRoom, LocalDateTime lastReadMessageTime) {
        return new ChatRoomMember(member, chatRoom, lastReadMessageTime);
    }

//    public boolean updateLastReadMessageTime(LocalDateTime lastReadMessageTime) {
//        if (lastReadMessageTime == null || lastReadMessageTime.isAfter(LocalDateTime.now())) {
//            return false;
//        }
//
//        if (this.lastReadMessageTime.isBefore(lastReadMessageTime)) {
//            this.lastReadMessageTime = lastReadMessageTime;
//            return true;
//        }
//        return false;
//    }

    public boolean updateLastReadMessageTime(LocalDateTime newTime) {
        if (newTime == null || newTime.isAfter(LocalDateTime.now())) {
            return false;
        }

        // (방어 코드) 혹시 모를 NullPointerException 방지
        if (this.lastReadMessageTime == null) {
            this.lastReadMessageTime = newTime;
            return true;
        }

        // 핵심 수정: newTime이 '이전'이 아니라면 (즉, 같거나 이후라면)
        if (!newTime.isBefore(this.lastReadMessageTime)) {

            // 최적화: 실제로 더 최신일 때만 DB 업데이트 수행
            if (newTime.isAfter(this.lastReadMessageTime)) {
                this.lastReadMessageTime = newTime;
            }

            // '같거나' '이후'이므로 "읽기 성공"으로 간주하고 true 반환
            return true;
        }

        // newTime이 oldTime보다 '이전'인 경우 (잘못된 요청)
        return false;
    }
}
