package com.samsamhajo.deepground.studyGroup.controller;

import com.samsamhajo.deepground.auth.security.CustomUserDetails;
import com.samsamhajo.deepground.global.success.SuccessResponse;
import com.samsamhajo.deepground.global.utils.GlobalLogger;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupKickRequest;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupMemberSummary;
import com.samsamhajo.deepground.studyGroup.service.StudyGroupMemberService;
import com.samsamhajo.deepground.studyGroup.success.StudyGroupSuccessCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/study-group")
public class StudyGroupMemberController {
  private final StudyGroupMemberService studyGroupMemberService;

  /**
   * 스터디 그룹 참가 요청 API
   *
   * @param studyGroupId 요청 대상 스터디 그룹 ID
   */
  @PostMapping("/{studyGroupId}/join")
  public ResponseEntity<SuccessResponse<?>> requestJoin(
      @PathVariable Long studyGroupId,
      @AuthenticationPrincipal CustomUserDetails customUserDetails
  ) {
    Member member = customUserDetails.getMember();
    GlobalLogger.info("스터디 참가 요청", member.getEmail(), "스터디 ID:", studyGroupId);

    studyGroupMemberService.requestToJoin(member, studyGroupId);

    return ResponseEntity
        .status(StudyGroupSuccessCode.REQUEST_JOIN_SUCCESS.getStatus())
        .body(SuccessResponse.of(StudyGroupSuccessCode.REQUEST_JOIN_SUCCESS));
  }

  @GetMapping("/{studyGroupId}/applicants")
  public ResponseEntity<SuccessResponse<List<StudyGroupMemberSummary>>> getPendingApplicants(
      @PathVariable Long studyGroupId,
      @AuthenticationPrincipal CustomUserDetails customUserDetails
  ) {
    Member requester = customUserDetails.getMember();
    GlobalLogger.info("스터디 신청자 목록 조회 요청", requester.getId(), studyGroupId);

    List<StudyGroupMemberSummary> response =
        studyGroupMemberService.getPendingApplicantsAsCreator(studyGroupId, requester);

    return ResponseEntity
        .status(StudyGroupSuccessCode.READ_SUCCESS.getStatus())
        .body(SuccessResponse.of(StudyGroupSuccessCode.READ_SUCCESS, response));
  }

  @GetMapping("/{studyGroupId}/members")
  public ResponseEntity<SuccessResponse<List<StudyGroupMemberSummary>>> getStudyGroupMembers(
      @PathVariable Long studyGroupId
  ) {
    GlobalLogger.info("스터디 참여자 목록 조회", studyGroupId);

    List<StudyGroupMemberSummary> response = studyGroupMemberService.getAcceptedMembers(studyGroupId);

    return ResponseEntity
        .status(StudyGroupSuccessCode.READ_SUCCESS.getStatus())
        .body(SuccessResponse.of(StudyGroupSuccessCode.READ_SUCCESS, response));
  }
}
