package com.samsamhajo.deepground.communityPlace.service;

import com.samsamhajo.deepground.communityPlace.entity.CommunityPlaceReview;
import com.samsamhajo.deepground.communityPlace.exception.CommunityPlaceErrorCode;
import com.samsamhajo.deepground.communityPlace.exception.CommunityPlaceException;
import com.samsamhajo.deepground.communityPlace.repository.CommunityPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommunityPlaceService {

    private final CommunityPlaceRepository communityPlaceRepository;

    public CommunityPlaceDto selectCommunityPlaceReviewsAndScope(Long specificAddressId) {

        CommunityPlaceReview communityPlaceReview = communityPlaceRepository.findByIdCountReviewsAndScopeAverage(specificAddressId)
                .orElseThrow(() -> new CommunityPlaceException(CommunityPlaceErrorCode.COMMUNITYPLACE_NOT_FOUND));

        return communityPlaceReview.
    }
}
