package com.samsamhajo.deepground.notification.entity;

public enum NotificationType {

    /**
     * 친구 알림
     */
    FRIEND_REQUEST,         // 친구 요청
    FRIEND_ACCEPT,          // 친구 요청 수락

    /**
     * 스터디 그룹 알림
     */
    STUDY_GROUP_KICK,       // 스터디 그룹 강퇴
    STUDY_GROUP_JOIN,       // 스터디 그룹 가입 요청
    STUDY_GROUP_ACCEPT,     // 스터디 그룹 가입 성공

    /**
     * 스케줄 알림
     */
    SCHEDULE_CREATE,        // 스터디 일정 생성
    SCHEDULE_REMINDER,      // 스터디 일정 알림

    /**
     * 피드 알림
     */
    FEED_COMMENT,           // 피드 댓글 알림
}
