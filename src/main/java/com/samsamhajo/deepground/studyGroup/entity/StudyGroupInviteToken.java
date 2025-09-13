package com.samsamhajo.deepground.studyGroup.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyGroupInviteToken {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "study_group_id", nullable = false)
  private StudyGroup studyGroup;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false, unique = true)
  private String token;

  @Column(nullable = false)
  private LocalDateTime expiresAt;

  public static StudyGroupInviteToken of(StudyGroup studyGroup, String email, String token, LocalDateTime expiresAt) {
    StudyGroupInviteToken entity = new StudyGroupInviteToken();
    entity.studyGroup = studyGroup;
    entity.email = email;
    entity.token = token;
    entity.expiresAt = expiresAt;
    return entity;
  }
}
