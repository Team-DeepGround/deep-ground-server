package com.samsamhajo.deepground.studyGroup.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyGroupAddress {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "study_group_address_id")
    private Long id;
}
