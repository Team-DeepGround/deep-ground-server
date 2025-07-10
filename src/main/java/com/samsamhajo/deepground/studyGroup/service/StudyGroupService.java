package com.samsamhajo.deepground.studyGroup.service;

import com.samsamhajo.deepground.chat.service.ChatRoomService;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupDetailResponse;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupParticipationResponse;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupMyListResponse;
import com.samsamhajo.deepground.studyGroup.entity.GroupStatus;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroupComment;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroupMemberStatus;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroupReply;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroupTechTag;
import com.samsamhajo.deepground.studyGroup.exception.StudyGroupNotFoundException;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupResponse;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupSearchRequest;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupTechTagRepository;
import com.samsamhajo.deepground.techStack.entity.TechStack;
import com.samsamhajo.deepground.techStack.repository.TechStackRepository;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import com.samsamhajo.deepground.chat.entity.ChatRoom;
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
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupUpdateRequest;

@Service
@RequiredArgsConstructor
public class StudyGroupService {

  private final StudyGroupRepository studyGroupRepository;
  private final StudyGroupMemberRepository studyGroupMemberRepository;
  private final ChatRoomService chatRoomService;
  private final TechStackRepository techStackRepository;
  private final StudyGroupTechTagRepository studyGroupTechTagRepository;


  @Transactional
  public StudyGroupDetailResponse getStudyGroupDetail(Long studyGroupId, Long memberId) {
    StudyGroup group = studyGroupRepository.findWithCreatorAndCommentsById(studyGroupId)
        .orElseThrow(() -> new StudyGroupNotFoundException(studyGroupId));

    List<Long> commentIds = group.getComments().stream()
        .map(StudyGroupComment::getId)
        .toList();

    List<StudyGroupReply> replies = studyGroupRepository.findRepliesByCommentIds(commentIds);

    Map<Long, List<StudyGroupReply>> replyMap = replies.stream()
        .collect(Collectors.groupingBy(r -> r.getComment().getId()));
    StudyGroupMemberStatus memberStatus = getMemberStatus(studyGroupId, memberId);

    return StudyGroupDetailResponse.from(group, replyMap, memberStatus);
  }

  public StudyGroupMemberStatus getMemberStatus(Long studyGroupId, Long memberId) {
    Optional<StudyGroupMember> memberOpt =
        studyGroupMemberRepository.findByStudyGroupIdAndMemberId(studyGroupId, memberId);

    return memberOpt.map(studyGroupMember -> studyGroupMember.getIsAllowed()
        ? StudyGroupMemberStatus.APPROVED
        : StudyGroupMemberStatus.PENDING).orElse(StudyGroupMemberStatus.NOT_APPLIED);

  }


  public Page<StudyGroupResponse> searchStudyGroups(StudyGroupSearchRequest request) {
    String keyword = request.getKeyword();
    GroupStatus status = request.getGroupStatus();
    Pageable pageable = request.toPageable();
    List<String> stackNames = request.getTechStackNames();

    Page<StudyGroup> pageResult = studyGroupRepository.searchWithFilters(status, keyword, stackNames, pageable);
    return pageResult.map(StudyGroupResponse::from);
  }

  private List<TechStack> getTechStacksByNames(List<String> names) {
    if (names == null || names.isEmpty()) return List.of();
    return techStackRepository.findByNames(names);
  }

  private void validatePeriod(LocalDate start, LocalDate end, String msg) {
    if (start.isAfter(end)) {
      throw new IllegalArgumentException(msg);
    }
  }

  @Transactional
  public StudyGroupDetailResponse updateStudyGroup(Long studyGroupId, StudyGroupUpdateRequest request, Member updater) {
    StudyGroup group = studyGroupRepository.findWithCreatorAndCommentsById(studyGroupId)
        .orElseThrow(() -> new StudyGroupNotFoundException(studyGroupId));

    if (!group.getCreator().getId().equals(updater.getId())) {
      throw new IllegalArgumentException("스터디 생성자만 수정할 수 있습니다.");
    }

    validatePeriod(request.getStudyStartDate(), request.getStudyEndDate(), "스터디 시작일은 종료일보다 이전이어야 합니다.");
    validatePeriod(request.getRecruitStartDate(), request.getRecruitEndDate(), "모집 시작일은 종료일보다 이전이어야 합니다.");

    if (request.getGroupMemberCount() <= 0) {
      throw new IllegalArgumentException("정원은 1명 이상이어야 합니다.");
    }

    List<TechStack> techStacks = getTechStacksByNames(request.getTechStackNames());
    group.update(request, techStacks);

    List<Long> commentIds = group.getComments().stream().map(c -> c.getId()).toList();
    List<StudyGroupReply> replies = studyGroupRepository.findRepliesByCommentIds(commentIds);
    Map<Long, List<StudyGroupReply>> replyMap = replies.stream().collect(Collectors.groupingBy(r -> r.getComment().getId()));
    StudyGroupMemberStatus memberStatus = getMemberStatus(group.getId(), updater.getId());

    return StudyGroupDetailResponse.from(group, replyMap, memberStatus);
  }

  @Transactional
  public StudyGroupCreateResponse createStudyGroup(StudyGroupCreateRequest request, Member creator) {
    validateRequest(request);
    ChatRoom chatRoom = chatRoomService.createStudyGroupChatRoom(creator);
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
        request.getStudyLocation()
    );
    StudyGroup savedGroup = studyGroupRepository.save(studyGroup);
    List<TechStack> techStacks = getTechStacksByNames(request.getTechStackNames());
    for (TechStack techStack : techStacks) {
      StudyGroupTechTag link = StudyGroupTechTag.of(savedGroup, techStack);
      studyGroupTechTagRepository.save(link);
    }
    StudyGroupMember groupMember = StudyGroupMember.of(creator, savedGroup, true);
    studyGroupMemberRepository.save(groupMember);
    return StudyGroupCreateResponse.from(savedGroup);
  }

  public List<StudyGroupParticipationResponse> getStudyGroupsByMember(Long memberId) {
    List<StudyGroupMember> studyGroupMembers =
        studyGroupMemberRepository.findAllByMemberIdAndIsAllowedTrueAndNotCreator(
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
