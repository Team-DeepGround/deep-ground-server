package com.samsamhajo.deepground.studyGroup.controller;

import com.samsamhajo.deepground.global.success.SuccessResponse;
import com.samsamhajo.deepground.global.utils.GlobalLogger;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.studyGroup.service.StudyGroupDeleteService;
import com.samsamhajo.deepground.studyGroup.success.StudyGroupSuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/study-group")
public class StudyGroupDeleteController {

  private final StudyGroupDeleteService deleteService;

  @DeleteMapping("/{studyGroupId}")
  public ResponseEntity<SuccessResponse<?>> deleteStudyGroup(
      @PathVariable Long studyGroupId,
      @RequestAttribute("member") Member requester
  ) {
    GlobalLogger.info("스터디 소프트 삭제 요청", studyGroupId, requester.getEmail());

    deleteService.softDeleteStudyGroup(studyGroupId, requester);

    return ResponseEntity
        .status(StudyGroupSuccessCode.DELETE_SUCCESS.getStatus())
        .body(SuccessResponse.of(StudyGroupSuccessCode.DELETE_SUCCESS));
  }
}