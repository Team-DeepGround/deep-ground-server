package com.samsamhajo.deepground.studyGroup.controller;

import com.samsamhajo.deepground.global.success.SuccessResponse;
import com.samsamhajo.deepground.global.utils.GlobalLogger;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupKickRequest;
import com.samsamhajo.deepground.studyGroup.service.StudyGroupKickService;
import com.samsamhajo.deepground.studyGroup.success.StudyGroupSuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/study-group")
public class StudyGroupKickController {

  private final StudyGroupKickService studyGroupKickService;

  @PostMapping("/kick")
  public ResponseEntity<SuccessResponse<?>> kickMember(
      @RequestBody @Valid StudyGroupKickRequest request,
      @RequestAttribute("member") Member requester
  ) {
    GlobalLogger.info("스터디 강퇴 요청", requester.getEmail(), request.getStudyGroupId(), request.getTargetMemberId());

    studyGroupKickService.kickMember(request, requester);

    return ResponseEntity
        .status(StudyGroupSuccessCode.UPDATE_SUCCESS.getStatus())
        .body(SuccessResponse.of(StudyGroupSuccessCode.UPDATE_SUCCESS));
  }
}

