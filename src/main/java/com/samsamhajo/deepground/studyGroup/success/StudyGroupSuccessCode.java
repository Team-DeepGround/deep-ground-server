package com.samsamhajo.deepground.studyGroup.success;

import com.samsamhajo.deepground.global.success.SuccessCode;
import org.springframework.http.HttpStatus;

public enum StudyGroupSuccessCode implements SuccessCode {
  CREATE_SUCCESS(HttpStatus.CREATED, "스터디 그룹이 성공적으로 생성되었습니다."),
  READ_SUCCESS(HttpStatus.OK, "스터디 그룹 상세 조회 성공");

  private final HttpStatus status;
  private final String message;

  StudyGroupSuccessCode(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
  }

  @Override
  public HttpStatus getStatus() {
    return status;
  }

  @Override
  public String getMessage() {
    return message;
  }
}