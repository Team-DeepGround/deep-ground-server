package com.samsamhajo.deepground.notification.entity.data;

import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
public class FriendRequestNotificationData extends NotificationData {

    @Field("nickname")
    private String nickname;

    private FriendRequestNotificationData(Long id, String nickname) {
        super(id);
        this.nickname = nickname;
    }

    public static FriendRequestNotificationData of(Long id, String nickname) {
        return new FriendRequestNotificationData(id, nickname);
    }
}
