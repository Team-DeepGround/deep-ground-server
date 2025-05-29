package com.samsamhajo.deepground.friend.Exception;

import com.samsamhajo.deepground.global.success.SuccessCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum FriendSuccessCode implements SuccessCode {
    FRIEND_SUCCESS_REQUEST(HttpStatus.OK, "친구 요청이 성공적으로 전송되었습니다."),
    FRIEND_SUCCESS_CANCEL(HttpStatus.OK, "친구 요청이 취소되었습니다 "),
    FRIEND_SUCCESS_REFUSAL(HttpStatus.OK,"친구 요청이 거절했습니다" ),
    FRIEND_SUCCESS_ACCEPT(HttpStatus.OK,"친구 요청을 수락했습니다" ),
    FRIEND_SENT_LIST_FOUND(HttpStatus.OK, "요청한 친구 요청 목록을 성공적으로 조회했습니다."),
    FRIEND_RECEIVE_LIST_FOUND(HttpStatus.OK, "수신한 친구 요청 목록을 성공적으로 조회했습니다."),
    FRIEND_SUCCESS_DELETE(HttpStatus.OK, "친구 목록에서 삭제되었습니다.");
    FRIEND_SUCCESS_GET_LIST(HttpStatus.OK,"친구 목록을 성공적으로 조회했습니다" );


    private final HttpStatus status;
    private final String message;

    FriendSuccessCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}

