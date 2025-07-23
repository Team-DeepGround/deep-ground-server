package com.samsamhajo.deepground.communityPlace.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "community_place_reviews")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommunityPlaceReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "communtiy_place_reviews_id")
    private Long id;

    @Column(name = "community_place_scope")
    private double scope;

    @Column(name = "community_place_content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "specific_address_id")
    @JsonBackReference //순환참조 방지 : (Depth 깊이 에러 발생)
    private SpecificAddress specificAddress;

    private CommunityPlaceReview(double scope,String content){
        this.scope = scope;
        this.content = content;
    }

    public static CommunityPlaceReview of(double scope,String content, SpecificAddress specificAddress) {
        CommunityPlaceReview review = new CommunityPlaceReview(scope, content);
        review.specificAddress = specificAddress;
        specificAddress.getCommunityPlaceReviews().add(review);
        return review;
    }
}
