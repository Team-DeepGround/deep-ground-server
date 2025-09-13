package com.samsamhajo.deepground.studyGroup.entity;

import com.samsamhajo.deepground.studyGroup.entity.StudyGroup;
import com.samsamhajo.deepground.techStack.entity.TechStack;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "study_group_tech_tag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyGroupTechTag {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "study_group_tech_tag_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "study_group_id", nullable = false)
  private StudyGroup studyGroup;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "tech_stack_id", nullable = false)
  private TechStack techStack;

  private StudyGroupTechTag(StudyGroup studyGroup, TechStack techStack) {
    this.studyGroup = studyGroup;
    this.techStack = techStack;
  }

  public static StudyGroupTechTag of(StudyGroup studyGroup, TechStack techStack) {
    StudyGroupTechTag link = new StudyGroupTechTag(studyGroup, techStack);
    studyGroup.addTechTag(link);
    return link;
  }
}