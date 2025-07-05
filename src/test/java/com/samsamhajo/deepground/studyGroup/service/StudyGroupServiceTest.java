package com.samsamhajo.deepground.studyGroup.service;

import com.samsamhajo.deepground.chat.entity.ChatRoom;
import com.samsamhajo.deepground.chat.entity.ChatRoomType;
import com.samsamhajo.deepground.chat.repository.ChatRoomRepository;
import com.samsamhajo.deepground.chat.service.ChatRoomService;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupCreateRequest;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupCreateResponse;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupMemberRepository;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupRepository;
import com.samsamhajo.deepground.techStack.repository.TechStackRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StudyGroupServiceTest {

    private StudyGroupRepository studyGroupRepository;
    private StudyGroupMemberRepository studyGroupMemberRepository;
    private ChatRoomService chatRoomService;
    private StudyGroupService studyGroupService;
    private TechStackRepository techStackRepository;

    @BeforeEach
    void setUp() {
        studyGroupRepository = mock(StudyGroupRepository.class);
        studyGroupMemberRepository = mock(StudyGroupMemberRepository.class);
        chatRoomService = mock(ChatRoomService.class);
        studyGroupService = new StudyGroupService(
                studyGroupRepository,
                studyGroupMemberRepository,
                chatRoomService,
                techStackRepository
        );
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
                .groupMemberCount(5)
                .isOffline(true)
                .studyLocation("강남")
                .build();
    }

    @Test
    void createStudyGroup_success() {
        // given
        Member creator = mock(Member.class);
        StudyGroupCreateRequest request = validRequest();
        ChatRoom chatRoom = ChatRoom.of(ChatRoomType.STUDY_GROUP);

        when(chatRoomService.createStudyGroupChatRoom(any())).thenReturn(chatRoom);
        when(studyGroupRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // when
        StudyGroupCreateResponse response = studyGroupService.createStudyGroup(request, creator);

        // then
        assertThat(response.getTitle()).isEqualTo(request.getTitle());
        assertThat(response.getIsOffline()).isEqualTo(true);

        verify(chatRoomService).createStudyGroupChatRoom(any());
        verify(studyGroupRepository).save(any());
        verify(studyGroupMemberRepository).save(any());
    }

    @Test
    void createStudyGroup_recruitEndDateInPast_throwsException() {
        // given
        Member creator = mock(Member.class);
        StudyGroupCreateRequest request = validRequest().toBuilder()
                .recruitEndDate(LocalDate.now().minusDays(1))
                .build();

        // when & then
        assertThatThrownBy(() -> studyGroupService.createStudyGroup(request, creator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("모집 마감일은 현재 시점보다 미래여야 합니다.");
    }

    @Test
    void createStudyGroup_startDateAfterEndDate_throwsException() {
        // given
        Member creator = mock(Member.class);
        StudyGroupCreateRequest request = validRequest().toBuilder()
                .studyStartDate(LocalDate.now().plusDays(20))
                .studyEndDate(LocalDate.now().plusDays(10))
                .build();

        // when & then
        assertThatThrownBy(() -> studyGroupService.createStudyGroup(request, creator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("스터디 시작일은 종료일보다 이전이어야 합니다.");
    }

    @Test
    void createStudyGroup_zeroMemberCount_throwsException() {
        // given
        Member creator = mock(Member.class);
        StudyGroupCreateRequest request = validRequest().toBuilder()
                .groupMemberCount(0)
                .build();

        // when & then
        assertThatThrownBy(() -> studyGroupService.createStudyGroup(request, creator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("정원은 1명 이상이어야 합니다.");
    }
}
