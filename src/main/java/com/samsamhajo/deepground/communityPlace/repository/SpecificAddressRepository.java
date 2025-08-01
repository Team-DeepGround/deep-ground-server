package com.samsamhajo.deepground.communityPlace.repository;

import com.samsamhajo.deepground.communityPlace.dto.CommunityPlaceReviewDto;
import com.samsamhajo.deepground.communityPlace.entity.CommunityPlaceReview;
import com.samsamhajo.deepground.communityPlace.entity.SpecificAddress;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpecificAddressRepository extends JpaRepository<SpecificAddress,Long> {

    @Query("SELECT AVG(cpr.scope), COUNT(cpr.content) FROM SpecificAddress sa " +
            "JOIN sa.communityPlaceReviews cpr " +
            "WHERE sa.id = :specificAddressId")
    Optional<CommunityPlaceReview> findByIdCountReviewsAndScopeAverage(@Param("specificAddressId") Long specificAddressId);

    @Query("SELECT sa.name, sa.number " +
            "FROM SpecificAddress sa " +
            "LEFT JOIN sa.communityPlaceReviews r " +
            "GROUP BY sa.name, sa.number " +
            "ORDER BY COUNT(r) DESC")
    List<CommunityPlaceReviewDto> findAllCommunityPlaceByReviewCountDesc();

    @Query("SELECT sa.name, sa.number " +
            "FROM SpecificAddress sa " +
            "LEFT JOIN sa.communityPlaceReviews r " +
            "GROUP BY sa.name, sa.number " +
            "ORDER BY AVG(r.scope) DESC")
    List<CommunityPlaceReviewDto> findAllCommunityPlaceByReviewScopeDesc();
}
