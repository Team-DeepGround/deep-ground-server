package com.samsamhajo.deepground.studyGroup.dto;

import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.studyGroup.entity.GroupStatus;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroup;
import com.samsamhajo.deepground.studyGroup.entity.TechTag;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Getter
@Builder
public class StudyGroupResponse {
  private Long id;
  private String title;
  private String description;
  private String period;
  private String recruitmentPeriod;
  private GroupStatus groupStatus;
  private Set<TechTag> tags;
  private Integer maxMembers;
  private Integer currentMembers;
  private OrganizerDto organizer;
  private Boolean isOnline;
  private String location;

  @Getter
  @Builder
  public static class OrganizerDto {
    private String name;
    private String avatar;
  }

  public static StudyGroupResponse from(StudyGroup group) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    Member creator = group.getCreator();

    return StudyGroupResponse.builder()
        .id(group.getId())
        .title(group.getTitle())
        .description(group.getExplanation())
        .period(group.getStudyStartDate().format(formatter) + " ~ " + group.getStudyEndDate().format(formatter))
        .recruitmentPeriod(group.getRecruitStartDate().format(formatter) + " ~ " + group.getRecruitEndDate().format(formatter))
        .tags(group.getTechTags())
        .maxMembers(group.getGroupMemberCount())
        .currentMembers(group.getMembers().size())
        .groupStatus(group.getGroupStatus())
        .organizer(
            OrganizerDto.builder()
                .name(creator.getNickname())
                .avatar("/placeholder.svg?height=40&width=40")
                .build()
        )
        .isOnline(!group.getIsOffline())
        .location(group.getStudyLocation())
        .build();
  }
}