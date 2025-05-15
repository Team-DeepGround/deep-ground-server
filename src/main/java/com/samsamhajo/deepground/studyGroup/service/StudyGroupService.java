package com.samsamhajo.deepground.studyGroup.service;

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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyGroupService {

  private final StudyGroupRepository studyGroupRepository;
  private final StudyGroupMemberRepository studyGroupMemberRepository;
  private final ChatRoomRepository chatRoomRepository;

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
        request.getStudyLocation()
    );

    StudyGroup savedGroup = studyGroupRepository.save(studyGroup);

    StudyGroupMember groupMember = StudyGroupMember.of(creator, savedGroup, true);
    studyGroupMemberRepository.save(groupMember);

    return StudyGroupCreateResponse.from(savedGroup);
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
