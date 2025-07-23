package com.samsamhajo.deepground.communityPlace.entity;

import com.samsamhajo.deepground.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "Community_place_reviews)")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommunityPlaceReview extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "communtiy_place_reviews_id")
    private Long id;

    @Column(name = "community_place_scope")
    private double scope;

    @Column(name = "community_place_content")
    private String content;

    private CommunityPlaceReview(double scope,String content){
        this.scope =scope;
        this.content = content;
    }
}
