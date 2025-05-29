package com.samsamhajo.deepground.studyGroup.controller;

import com.samsamhajo.deepground.global.success.SuccessResponse;
import com.samsamhajo.deepground.global.utils.GlobalLogger;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupParticipationResponse;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupSearchRequest;
import com.samsamhajo.deepground.studyGroup.service.StudyGroupJoinService;
import com.samsamhajo.deepground.studyGroup.service.StudyGroupService;
import com.samsamhajo.deepground.studyGroup.success.StudyGroupSuccessCode;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupCreateRequest;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupCreateResponse;

import jakarta.validation.Valid;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/study-group")
public class StudyGroupController {

  private final StudyGroupService studyGroupService;
  private final StudyGroupJoinService studyGroupJoinService;

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

  @GetMapping("/search")
  public ResponseEntity<SuccessResponse<?>> searchStudyGroups(
      @ModelAttribute StudyGroupSearchRequest request
  ) {
    GlobalLogger.info("스터디 목록 검색 요청", request.getKeyword(), request.getGroupStatus());

    var response = studyGroupService.searchStudyGroups(request);
    return ResponseEntity
        .status(StudyGroupSuccessCode.SEARCH_SUCCESS.getStatus())
        .body(SuccessResponse.of(StudyGroupSuccessCode.READ_SUCCESS, response));
  }

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



  /**
   * 스터디 그룹 참가 요청 API
   * @param studyGroupId 요청 대상 스터디 그룹 ID
   * @param member 요청자 (JWT 인증 필터를 통해 주입됨)
   */
  @PostMapping("/{studyGroupId}/join")
  public ResponseEntity<SuccessResponse<?>> requestJoin(
      @PathVariable Long studyGroupId,
      @RequestAttribute("member") Member member
  ) {
    GlobalLogger.info("스터디 참가 요청", member.getEmail(), "스터디 ID:", studyGroupId);

    studyGroupJoinService.requestToJoin(member, studyGroupId);

    return ResponseEntity
        .status(StudyGroupSuccessCode.REQUEST_JOIN_SUCCESS.getStatus())
        .body(SuccessResponse.of(StudyGroupSuccessCode.REQUEST_JOIN_SUCCESS));
  }

  @GetMapping("/{memberId}/joined")
  public ResponseEntity<SuccessResponse<List<StudyGroupParticipationResponse>>> getMyParticipatedGroups(
      @PathVariable Long memberId
  ) {
    GlobalLogger.info("참가 중인 스터디 목록 조회", memberId);

    List<StudyGroupParticipationResponse> response =
        studyGroupService.getStudyGroupsByMember(memberId);

  @GetMapping("/my/{memberId}")
  public ResponseEntity<SuccessResponse<?>> getMyStudyGroups(
      @PathVariable Long memberId
  ) {
    GlobalLogger.info("사용자 생성 스터디 목록 조회 요청", memberId);

    var response = studyGroupService.findMyStudyGroups(memberId);

    return ResponseEntity
        .status(StudyGroupSuccessCode.READ_SUCCESS.getStatus())
        .body(SuccessResponse.of(StudyGroupSuccessCode.READ_SUCCESS, response));
  }
}
