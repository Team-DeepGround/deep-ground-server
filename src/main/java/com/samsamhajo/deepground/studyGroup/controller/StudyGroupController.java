package com.samsamhajo.deepground.studyGroup.controller;

import com.samsamhajo.deepground.global.success.SuccessResponse;
import com.samsamhajo.deepground.global.utils.GlobalLogger;
import com.samsamhajo.deepground.studyGroup.service.StudyGroupService;
import com.samsamhajo.deepground.studyGroup.success.StudyGroupSuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/study-group")
public class StudyGroupController {

  private final StudyGroupService studyGroupService;

  @GetMapping("/{studyGroupId}")
  public ResponseEntity<SuccessResponse<?>> getStudyGroupDetail(
      @PathVariable Long studyGroupId
  ) {
    GlobalLogger.info("스터디 그룹 상세 조회 요청", studyGroupId);

    var response = studyGroupService.getStudyGroupDetail(studyGroupId);

    return ResponseEntity
        .status(StudyGroupSuccessCode.READ_SUCCESS.getStatus())
        .body(SuccessResponse.of(StudyGroupSuccessCode.READ_SUCCESS, response));
  }
}
