package com.samsamhajo.deepground.studyGroup.service;

import com.samsamhajo.deepground.IntegrationTestSupport;
import com.samsamhajo.deepground.chat.service.ChatRoomService;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupCreateRequest;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupCreateResponse;
import com.samsamhajo.deepground.studyGroup.entity.GroupStatus;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroup;
import com.samsamhajo.deepground.studyGroup.exception.StudyGroupNotFoundException;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupMemberRepository;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupRepository;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupTechTagRepository;
import com.samsamhajo.deepground.techStack.repository.TechStackRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
public class StudyGroupTest extends IntegrationTestSupport {

    @Autowired
    private StudyGroupRepository studyGroupRepository;

    @Autowired
    private StudyGroupService studyGroupService;

    @Autowired
    private MemberRepository memberRepository;

    private Member creater;

    @BeforeEach
    void setUp() {
        creater = Member.createLocalMember("paka@gamil.com", "pw", "파카");
        memberRepository.save(creater);
    }

    private StudyGroupCreateRequest validRequest() {
        LocalDate now = LocalDate.now();
        return StudyGroupCreateRequest.builder()
                .title("Java 스터디")
                .explanation("자바 스터디 모임입니다.")
                .studyStartDate(now.plusDays(10))
                .studyEndDate(now.plusDays(30))
                .recruitStartDate(now)
                .recruitEndDate(now.plusDays(5))
                .maxMembers(5)
                .isOffline(true)
                .studyLocation("강남")
                .build();
    }

    @Test
    @DisplayName("날짜에 따른 스터디 그룹 상태값 변경 테스트")
    void changeStudyGroupStatusByDate() throws Exception {
        //given
        StudyGroupCreateRequest request = validRequest().toBuilder()
                .recruitEndDate(LocalDate.now())
                .studyStartDate(LocalDate.now().plusDays(1))
                .studyEndDate(LocalDate.now().plusDays(10))
                .build();
        StudyGroupCreateResponse studyGroupDto= studyGroupService.createStudyGroup(request,creater);
        Long studyGroupId = studyGroupDto.getId();
        //when
        studyGroupService.updateStudyGroupStatus(LocalDate.now().plusDays(1));

        //then
        StudyGroup studyGroup = studyGroupRepository.findById(studyGroupId)
                .orElseThrow(() -> new StudyGroupNotFoundException(studyGroupId));
        assertEquals(GroupStatus.ONGOING,studyGroup.getGroupStatus());
    }

    @Test
    @DisplayName("스터디 그룹 종료일이 지난 후 상태값 변경 테스트")
    void changeStudyGroupStatusByEndDate() throws Exception {
        //given
        StudyGroupCreateRequest request = validRequest().toBuilder()
                .recruitEndDate(LocalDate.now())
                .studyStartDate(LocalDate.now().plusDays(1))
                .studyEndDate(LocalDate.now().plusDays(10))
                .build();
        StudyGroupCreateResponse studyGroupDto= studyGroupService.createStudyGroup(request,creater);
        Long studyGroupId = studyGroupDto.getId();
        //when
        studyGroupService.updateStudyGroupStatus(LocalDate.now().plusDays(11));

        //then
        StudyGroup studyGroup = studyGroupRepository.findById(studyGroupId)
                .orElseThrow(() -> new StudyGroupNotFoundException(studyGroupId));
        assertEquals(GroupStatus.COMPLETED,studyGroup.getGroupStatus());
    }
}
