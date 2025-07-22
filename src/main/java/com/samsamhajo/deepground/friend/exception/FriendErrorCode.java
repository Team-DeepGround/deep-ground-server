package com.samsamhajo.deepground.friend.Exception;

import com.samsamhajo.deepground.global.error.core.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;


@AllArgsConstructor
public enum FriendErrorCode implements ErrorCode {

    ALREADY_REQUESTED(HttpStatus.BAD_REQUEST, "이미 친구 요청을 보낸 사용자입니다."),
    SELF_REQUEST(HttpStatus.BAD_REQUEST, "자기 자신에게는 친구 요청을 보낼 수 없습니다."),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST, "올바른 이메일 형식을 입력해주세요."),
    BLANK_EMAIL(HttpStatus.BAD_REQUEST, "친구요청을 보낼 이메일을 입력해주세요"),
    ALREADY_FRIEND(HttpStatus.BAD_REQUEST, "이미 친구로 등록된 사용자입니다."),
    INVALID_FRIEND_REQUEST(HttpStatus.BAD_REQUEST, "친구 요청이 존재하지 않습니다"),
    UNAUTHORIZED_ACCESS(HttpStatus.FORBIDDEN, "본인의 친구 관계만 응답할 수 있습니다."),
    REQUEST_ALREADY_RECEIVED(HttpStatus.BAD_REQUEST, "상대방이 이미 친구 요청을 보냈습니다"),
    INVALID_FRIEND(HttpStatus.BAD_REQUEST,"존재하지 않은 친구입니다");


    private final HttpStatus status;
    private final String message;

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return "[FRIEND ERROR]" + message;
    }
}
