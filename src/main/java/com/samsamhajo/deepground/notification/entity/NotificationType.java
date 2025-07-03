package com.samsamhajo.deepground.notification.entity;

public enum NotificationType {
    FRIEND_REQUEST,         // 친구 요청
    FRIEND_ACCEPT,          // 친구 요청 수락
    STUDY_GROUP_KICK,       // 스터디 그룹 강퇴
    STUDY_GROUP_JOIN,       // 스터디 그룹 가입 요청
    STUDY_GROUP_ACCEPT,     // 스터디 그룹 가입 성공
    SCHEDULE_CREATE,        // 스터디 일정 생성
    SCHEDULE_REMINDER,      // 스터디 일정 알림
    NEW_MESSAGE,            // 새 메시지

    // TODO: QNA, FEED 알림
}
