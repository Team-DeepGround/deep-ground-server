package com.samsamhajo.deepground.studyGroup.service;

import com.samsamhajo.deepground.studyGroup.dto.StudyGroupDetailResponse;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupParticipationResponse;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupMyListResponse;
import com.samsamhajo.deepground.studyGroup.entity.GroupStatus;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroupComment;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroupReply;
import com.samsamhajo.deepground.studyGroup.exception.StudyGroupNotFoundException;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupResponse;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupSearchRequest;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import com.samsamhajo.deepground.chat.entity.ChatRoom;
import com.samsamhajo.deepground.chat.entity.ChatRoomType;
import com.samsamhajo.deepground.chat.repository.ChatRoomRepository;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupCreateRequest;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupCreateResponse;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroup;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroupMember;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupMemberRepository;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyGroupService {

  private final StudyGroupRepository studyGroupRepository;
  private final StudyGroupMemberRepository studyGroupMemberRepository;
  private final ChatRoomRepository chatRoomRepository;


  @Transactional
  public StudyGroupDetailResponse getStudyGroupDetail(Long studyGroupId) {
    StudyGroup group = studyGroupRepository.findWithCreatorAndCommentsById(studyGroupId)
        .orElseThrow(() -> new StudyGroupNotFoundException(studyGroupId));

    List<Long> commentIds = group.getComments().stream()
        .map(StudyGroupComment::getId)
        .toList();

    List<StudyGroupReply> replies = studyGroupRepository.findRepliesByCommentIds(commentIds);

    Map<Long, List<StudyGroupReply>> replyMap = replies.stream()
        .collect(Collectors.groupingBy(r -> r.getComment().getId()));

    return StudyGroupDetailResponse.from(group, replyMap);
  }


  public Page<StudyGroupResponse> searchStudyGroups(StudyGroupSearchRequest request) {
    String keyword = request.getKeyword();
    GroupStatus status = request.getGroupStatus();
    Pageable pageable = request.toPageable();

    List<String> tagNames = null;
    if (request.getTechTags() != null && !request.getTechTags().isEmpty()) {
      tagNames = request.getTechTags().stream()
          .map(Enum::name) // enum -> String
          .toList();
    }

    Page<StudyGroup> pageResult = studyGroupRepository.searchWithFilters(status, keyword, tagNames, pageable);
    return pageResult.map(StudyGroupResponse::from);
  }

  @Transactional
  public StudyGroupCreateResponse createStudyGroup(StudyGroupCreateRequest request, Member creator) {
    validateRequest(request);

    ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.of(ChatRoomType.STUDY_GROUP));

    StudyGroup studyGroup = StudyGroup.of(
        chatRoom,
        request.getTitle(),
        request.getExplanation(),
        request.getStudyStartDate(),
        request.getStudyEndDate(),
        request.getRecruitStartDate(),
        request.getRecruitEndDate(),
        request.getGroupMemberCount(),
        creator,
        request.getIsOffline(),
        request.getStudyLocation(),
        request.getTechTags()
    );

    StudyGroup savedGroup = studyGroupRepository.save(studyGroup);

    StudyGroupMember groupMember = StudyGroupMember.of(creator, savedGroup, true);
    studyGroupMemberRepository.save(groupMember);

    return StudyGroupCreateResponse.from(savedGroup);
  }

  public List<StudyGroupParticipationResponse> getStudyGroupsByMember(Long memberId) {
    List<StudyGroupMember> studyGroupMembers =
        studyGroupMemberRepository.findAllByMemberIdAndIsAllowedTrueOrderByStudyGroupCreatedAtDesc(
            memberId);

    return studyGroupMembers.stream()
        .map(member -> StudyGroupParticipationResponse.from(
            member.getStudyGroup(),
            member.getCreatedAt()
        )).toList();
  }
      
  public List<StudyGroupMyListResponse> findMyStudyGroups(Long memberId) {
    List<StudyGroup> groups = studyGroupRepository.findAllByCreator_IdOrderByCreatedAtDesc(memberId);

    return groups.stream()
        .map(StudyGroupMyListResponse::from)
        .toList();
  }


  private void validateRequest(StudyGroupCreateRequest request) {
    LocalDate now = LocalDate.now();

    if (request.getRecruitEndDate().isBefore(now)) {
      throw new IllegalArgumentException("모집 마감일은 현재 시점보다 미래여야 합니다.");
    }

    if (request.getStudyStartDate().isAfter(request.getStudyEndDate())) {
      throw new IllegalArgumentException("스터디 시작일은 종료일보다 이전이어야 합니다.");
    }

    if (request.getGroupMemberCount() <= 0) {
      throw new IllegalArgumentException("정원은 1명 이상이어야 합니다.");
    }

    // 필요한 경우 추가 유효성 체크 가능
  }
}
