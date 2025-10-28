package com.samsamhajo.deepground.member.service;

import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.exception.MemberErrorCode;
import com.samsamhajo.deepground.member.exception.MemberException;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import com.samsamhajo.deepground.studyGroup.dto.MemberStudySummaryResponse;
import com.samsamhajo.deepground.studyGroup.dto.TagDto;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroup;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroupTechTag;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberStudyService {

    private final StudyGroupRepository studyGroupRepository;
    private final MemberRepository memberRepository;

    public Page<MemberStudySummaryResponse> getMemberStudies(Long memberId, Pageable pageable) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        // 해당 멤버가 참여한 스터디 목록 조회
        Page<StudyGroup> page = studyGroupRepository.findAllCreatedOrJoinedByMemberId(member.getId(), pageable);

        return page.map(this::toDto);
    }

    private MemberStudySummaryResponse toDto(StudyGroup g) {
        int currentMembers = (g.getMembers() != null) ? g.getMembers().size() : 0;

        List<TagDto> tags = (g.getStudyGroupTechTags() == null)
                ? List.of()
                : g.getStudyGroupTechTags().stream()
                .map(StudyGroupTechTag::getTechStack)
                .filter(Objects::nonNull)
                .map(ts -> new TagDto(ts.getId(), ts.getName()))
                .collect(Collectors.toList());

        return new MemberStudySummaryResponse(
                g.getId(),
                g.getTitle(),
                g.getExplanation(),
                g.getIsOffline(),
                g.getStudyLocation(),
                g.getStudyStartDate(),
                g.getStudyEndDate(),
                g.getRecruitStartDate(),
                g.getRecruitEndDate(),
                g.getGroupMemberCount(),
                currentMembers,
                g.getGroupStatus() != null ? g.getGroupStatus().name() : null,
                tags
        );
    }
}
