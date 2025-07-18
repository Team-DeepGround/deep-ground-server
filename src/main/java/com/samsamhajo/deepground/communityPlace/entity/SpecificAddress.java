package com.samsamhajo.deepground.communityPlace.entity;

import com.samsamhajo.deepground.calendar.entity.StudySchedule;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SpecificAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="specific_address_id")
    private Long id;

    @Column(name="specific_address_location")
    private String location;

    @Column(name="specific_address_location_point")
    private Point locationPoint;

    @OneToMany
    @JoinColumn(name = "specific_address_id")
    private List<StudySchedule> studySchedules = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "speicfic_address_id")
    private List<CommunityPlaceReview> communityPlaceReviews = new ArrayList<>();

    private SpecificAddress(String location,Point locationPoint){
        this.location = location;
        this.locationPoint = locationPoint;
    }

}
