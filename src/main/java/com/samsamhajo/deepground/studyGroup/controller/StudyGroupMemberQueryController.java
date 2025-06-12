package com.samsamhajo.deepground.studyGroup.controller;

import com.samsamhajo.deepground.global.success.SuccessResponse;
import com.samsamhajo.deepground.global.utils.GlobalLogger;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupMemberSummary;
import com.samsamhajo.deepground.studyGroup.service.StudyGroupMemberQueryService;
import com.samsamhajo.deepground.studyGroup.success.StudyGroupSuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/study-group")
public class StudyGroupMemberQueryController {

  private final StudyGroupMemberQueryService queryService;

  @GetMapping("/{studyGroupId}/members")
  public ResponseEntity<SuccessResponse<List<StudyGroupMemberSummary>>> getStudyGroupMembers(
      @PathVariable Long studyGroupId
  ) {
    GlobalLogger.info("스터디 참여자 목록 조회", studyGroupId);

    List<StudyGroupMemberSummary> response = queryService.getAcceptedMembers(studyGroupId);

    return ResponseEntity
        .status(StudyGroupSuccessCode.READ_SUCCESS.getStatus())
        .body(SuccessResponse.of(StudyGroupSuccessCode.READ_SUCCESS, response));
  }
}
