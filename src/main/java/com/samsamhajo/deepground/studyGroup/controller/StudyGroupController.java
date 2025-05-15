package com.samsamhajo.deepground.studyGroup.controller;

import com.samsamhajo.deepground.global.success.SuccessResponse;
import com.samsamhajo.deepground.global.utils.GlobalLogger;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupSearchRequest;
import com.samsamhajo.deepground.studyGroup.service.StudyGroupService;
import com.samsamhajo.deepground.studyGroup.success.StudyGroupSuccessCode;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupCreateRequest;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupCreateResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/study-group")
public class StudyGroupController {

  private final StudyGroupService studyGroupService;

  @GetMapping("/search")
  public ResponseEntity<SuccessResponse<?>> searchStudyGroups(
      @ModelAttribute StudyGroupSearchRequest request
  ) {
    GlobalLogger.info("스터디 목록 검색 요청", request.getKeyword(), request.getGroupStatus());

    var response = studyGroupService.searchStudyGroups(request);

  @PostMapping
  public ResponseEntity<SuccessResponse<StudyGroupCreateResponse>> createStudyGroup(
      @RequestBody @Valid StudyGroupCreateRequest request,
      @RequestAttribute("member") Member member
  ) {
    GlobalLogger.info("스터디 생성 요청", member.getEmail(), request.getTitle());

    StudyGroupCreateResponse response = studyGroupService.createStudyGroup(request, member);

    return ResponseEntity
        .status(StudyGroupSuccessCode.CREATE_SUCCESS.getStatus())
        .body(SuccessResponse.of(StudyGroupSuccessCode.CREATE_SUCCESS, response));
  }
}
