package com.samsamhajo.deepground.communityPlace.service;

import com.samsamhajo.deepground.communityPlace.entity.CommunityPlaceReview;
import com.samsamhajo.deepground.communityPlace.exception.CommunityPlaceErrorCode;
import com.samsamhajo.deepground.communityPlace.exception.CommunityPlaceException;
import com.samsamhajo.deepground.communityPlace.repository.CommunityPlaceRepository;
import com.samsamhajo.deepground.communityPlace.repository.SpecificAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommunityPlaceService {

    private final CommunityPlaceRepository communityPlaceRepository;
    private final SpecificAddressRepository specificAddressRepository;

    public CommunityPlaceReview selectCommunityPlaceReviewsAndScope(Long specificAddressId) {

        CommunityPlaceReview communityPlaceReview = specificAddressRepository.findByIdCountReviewsAndScopeAverage(specificAddressId)
                .orElseThrow(() -> new CommunityPlaceException(CommunityPlaceErrorCode.COMMUNITYPLACE_NOT_FOUND));

        return communityPlaceReview;
    }
    //TODO: 리뷰 작성 로직 구현 후 테스트 코드 작성 후 테스트 및 SWAGGER 통해 컨트롤러 테스트 진행 예정
}