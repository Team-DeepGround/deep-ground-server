package com.samsamhajo.deepground.studyGroup.controller;

import com.samsamhajo.deepground.auth.security.CustomUserDetails;
import com.samsamhajo.deepground.global.success.SuccessResponse;
import com.samsamhajo.deepground.global.utils.GlobalLogger;
import com.samsamhajo.deepground.studyGroup.dto.CalculatedStudyGroupsInLocalResponse;
import com.samsamhajo.deepground.studyGroup.service.CalculatedStudyGroupInLocalService;
import com.samsamhajo.deepground.studyGroup.success.StudyGroupSuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/study-group/calculate")
public class CalculatedStudyGroupsInLocalController {

  private final CalculatedStudyGroupInLocalService calculatedStudyGroupInLocalService;
  @GetMapping
  public ResponseEntity<SuccessResponse<?>> getStudyGroupDetail(
      @RequestParam String city,
      @RequestParam String gu,
      @AuthenticationPrincipal CustomUserDetails customUserDetails
  ) {
    GlobalLogger.info("스터디 그룹 집계 조회", city, gu);

    CalculatedStudyGroupsInLocalResponse response =
            calculatedStudyGroupInLocalService.getStudyGroupsInLocal(city, gu);

    return ResponseEntity
        .status(StudyGroupSuccessCode.READ_SUCCESS.getStatus())
        .body(SuccessResponse.of(StudyGroupSuccessCode.READ_SUCCESS, response));
  }
}