package com.samsamhajo.deepground.communityPlace.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.samsamhajo.deepground.calendar.entity.StudySchedule;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.Point;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "specific_address")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SpecificAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="specific_address_id")
    private Long id;

    @Column(name="specific_address_location")
    private String location;

    @Column(name = "specific_address_location_point", columnDefinition = "POINT")
    @JdbcTypeCode(SqlTypes.GEOMETRY)
    private Point locationPoint;

    @OneToMany
    @JoinColumn(name = "specific_address_id")
    private List<StudySchedule> studySchedules = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "specific_address_id")
    @JsonManagedReference //순환참조 방지
    private List<CommunityPlaceReview> communityPlaceReviews = new ArrayList<>();

    private SpecificAddress(String location,Point locationPoint){
        this.location = location;
        this.locationPoint = locationPoint;
    }

    public static SpecificAddress of(String location,Point locationPoint){
        return new SpecificAddress(location,locationPoint);
    }

}
