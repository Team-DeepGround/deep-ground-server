package com.samsamhajo.deepground.studyGroup.entity;

import com.samsamhajo.deepground.address.entity.Address;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyGroupAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_group_address_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_group_id")
    private StudyGroup studyGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;

    private StudyGroupAddress(StudyGroup studyGroup, Address address) {
        this.studyGroup = studyGroup;
        this.address = address;
    }

    public static StudyGroupAddress of(StudyGroup studyGroup, Address address) {
        return new StudyGroupAddress(studyGroup, address);
    }

    public void assignStudyGroup(StudyGroup studyGroup) {
        if (this.studyGroup != null) {
            this.studyGroup.getStudyGroupAddresses().remove(this);
        }
        this.studyGroup = studyGroup;
        if (studyGroup != null) {
            studyGroup.getStudyGroupAddresses().add(this);
        }
    }
}
