package com.samsamhajo.deepground.notification.entity.data;

import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
public class FriendRequestNotificationData extends NotificationData {

    @Field("nickname")
    private String nickname;
}
