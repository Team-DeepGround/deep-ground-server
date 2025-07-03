package com.samsamhajo.deepground.notification.entity.data;

import com.samsamhajo.deepground.feed.feedcomment.entity.FeedComment;
import com.samsamhajo.deepground.notification.entity.NotificationData;
import com.samsamhajo.deepground.notification.entity.NotificationType;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
public class FeedNotificationData extends NotificationData {

    @Field("feedId")
    private Long feedId;

    @Field("content")
    private String content;

    public FeedNotificationData(NotificationType type, Long feedId, String content) {
        super(type);
        this.feedId = feedId;
        this.content = content;
    }

    public static FeedNotificationData comment(FeedComment feedComment) {
        return new FeedNotificationData(
                NotificationType.FEED_COMMENT,
                feedComment.getFeed().getId(),
                truncated(feedComment.getContent())
        );
    }
}
