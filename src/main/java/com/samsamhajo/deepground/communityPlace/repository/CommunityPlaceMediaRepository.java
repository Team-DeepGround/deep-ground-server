package com.samsamhajo.deepground.communityPlace.repository;

import com.samsamhajo.deepground.communityPlace.entity.CommunityPlaceMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommunityPlaceMediaRepository extends JpaRepository<CommunityPlaceMedia, Long> {

    List<CommunityPlaceMedia> findAllByCommunityPlaceReviewId(Long communityPlaceReviewId);
    void deleteAllByCommunityPlaceReviewId(Long communityPlaceReviewId);

}
