package com.samsamhajo.deepground.notification.entity.data;

import com.samsamhajo.deepground.notification.entity.NotificationData;
import com.samsamhajo.deepground.notification.entity.NotificationType;
import com.samsamhajo.deepground.qna.answer.entity.Answer;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
public class QNANotificationData extends NotificationData {

    @Field("questionId")
    private Long questionId;

    @Field("content")
    private String content;

    protected QNANotificationData() {
        super(null); // (부모 클래스의 기본 생성자 호출)
    }

    public QNANotificationData(NotificationType type, Long questionId, String content) {
        super(type);
        this.questionId = questionId;
        this.content = content;
    }

    public static QNANotificationData answer(Answer answer) {
        return new QNANotificationData(
                NotificationType.QNA_ANSWER,
                answer.getQuestion().getId(),
                truncated(answer.getAnswerContent())
        );
    }

    public static QNANotificationData comment(Long questionId, String content) {
        return new QNANotificationData(
                NotificationType.QNA_COMMENT,
                questionId,
                truncated(content)
        );
    }
}
