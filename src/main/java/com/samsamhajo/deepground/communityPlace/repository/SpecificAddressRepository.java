package com.samsamhajo.deepground.communityPlace.repository;

import com.samsamhajo.deepground.communityPlace.entity.CommunityPlaceReview;
import com.samsamhajo.deepground.communityPlace.entity.SpecificAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpecificAddressRepository extends JpaRepository<SpecificAddress,Long> {

    @Query("SELECT AVG(cpr.scope), COUNT(cpr.content) FROM SpecificAddress sa " +
            "JOIN sa.communityPlaceReviews cpr " +
            "WHERE sa.id = :specificAddressId")
    Optional<CommunityPlaceReview> findByIdCountReviewsAndScopeAverage(@Param("specificAddressId") Long specificAddressId);
}
