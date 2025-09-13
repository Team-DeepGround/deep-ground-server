package com.samsamhajo.deepground.qna.answer.exception;

import com.samsamhajo.deepground.global.success.SuccessCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AnswerSuccessCode implements SuccessCode {

    ANSWER_CREATED(HttpStatus.CREATED, "답변이 성공적으로 생성되었습니다."),
    ANSWER_DELETED(HttpStatus.OK,"답변이 성공적으로 삭제되었습니다."),
    ANSWER_UPDATED(HttpStatus.OK, "답변이 성공적으로 수정되었습니다."),
    ANSWER_LIKED(HttpStatus.OK, "답변 좋아요가 추가되었습니다."),
    ANSWER_UNLIKED(HttpStatus.OK, "답변 좋아요가 취소되었습니다."),
    ANSWER_SUCCESS_CODE(HttpStatus.OK, "답변이 성공적으로 조회되었습니다.");

    private final HttpStatus status;
    private final String message;

    AnswerSuccessCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}