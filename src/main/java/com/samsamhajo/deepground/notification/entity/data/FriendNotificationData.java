package com.samsamhajo.deepground.notification.entity.data;

import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.notification.entity.NotificationData;
import com.samsamhajo.deepground.notification.entity.NotificationType;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
public class FriendNotificationData extends NotificationData {

    @Field("memberId")
    private Long memberId;

    @Field("nickname")
    private String nickname;

    private FriendNotificationData(NotificationType type, Long memberId, String nickname) {
        super(type);
        this.memberId = memberId;
        this.nickname = nickname;
    }

    public static FriendNotificationData request(Member member) {
        return new FriendNotificationData(
                NotificationType.FRIEND_REQUEST,
                member.getId(),
                member.getNickname()
        );
    }

    public static FriendNotificationData accept(Member member) {
        return new FriendNotificationData(
                NotificationType.FRIEND_ACCEPT,
                member.getId(),
                member.getNickname()
        );
    }
}
