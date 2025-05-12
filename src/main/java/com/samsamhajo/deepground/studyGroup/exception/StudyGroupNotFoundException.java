package com.samsamhajo.deepground.studyGroup.exception;

public class StudyGroupNotFoundException extends RuntimeException {
  public StudyGroupNotFoundException(Long id) {
    super("해당 ID의 스터디 그룹을 찾을 수 없습니다: " + id);
  }
}
