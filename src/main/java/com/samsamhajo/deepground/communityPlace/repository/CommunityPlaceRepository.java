package com.samsamhajo.deepground.communityPlace.repository;

import com.samsamhajo.deepground.communityPlace.entity.CommunityPlaceReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommunityPlaceRepository extends JpaRepository<CommunityPlaceReview,Long> {

    @Query("SELECT AVG(cpr.scope), COUNT(cpr.content) FROM CommunityPlaceReviews cpr " +
            "JOIN FETCH SpecificAddress sa " +
            "WHERE cpr.communityPlaceReviewsId = :communityPlaceReviewsId" +
            "AND sa.locationPoint = :locationPoint" )
    Optional<Object> findByIdCountReviewsAndScopeAverage(Long specificAddressId);
}
