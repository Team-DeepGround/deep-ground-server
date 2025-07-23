package com.samsamhajo.deepground.communityPlace.service;

import com.samsamhajo.deepground.communityPlace.dto.request.AddressDto;
import com.samsamhajo.deepground.communityPlace.dto.request.CreateReviewDto;
import com.samsamhajo.deepground.communityPlace.dto.response.ReviewResponseDto;
import com.samsamhajo.deepground.communityPlace.entity.CommunityPlaceReview;
import com.samsamhajo.deepground.communityPlace.entity.SpecificAddress;
import com.samsamhajo.deepground.communityPlace.repository.CommunityPlaceRepository;
import com.samsamhajo.deepground.communityPlace.repository.SpecificAddressRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommunityPlaceService {

    private final CommunityPlaceRepository communityPlaceRepository;
    private final ValidService validService;
    private final CommunityPlaceMediaService communityPlaceMediaService;
    private final SpecificAddressRepository specificAddressRepository;

    @Transactional
    public ReviewResponseDto createReview(CreateReviewDto createReviewDto, Long memberId) {

        // Member가 존재하는지 여부 검증
        validService.findMemberById(memberId);

        //AddressDto를 통해 주소, 좌표값 SpecificAddress로 저장
        AddressDto dto = createReviewDto.getAddress();

        System.out.println("위치 주소: " + dto.getAddress());
        System.out.println("위도: " + dto.getLatitude());
        System.out.println("경도: " + dto.getLongitude());

        // SRID 4326 지정: WGS84 (MySQL이 기대하는 좌표계)
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

        // dto 내부에 아래 메서드가 있어야 함
        Point point = dto.toPoint(geometryFactory);

        // 이후 저장
        SpecificAddress address = specificAddressRepository.save(
                SpecificAddress.of(dto.getAddress(), point)
        );


        //SpecificAddress 저장 후, Review 생성
        CommunityPlaceReview review = CommunityPlaceReview.of(
                createReviewDto.getScope(),
                createReviewDto.getContent(),
                address
        );

        // 별점과 리뷰 CommunityPlaceReview에 저장
        communityPlaceRepository.save(review);

        //mediaUrl 저장
        List<String> mediaUrl = createCommunityPlaceMedia(createReviewDto, review);

        //ReviewResponseDto로 반환
        return ReviewResponseDto.of(
                review.getId(),
                review.getScope(),
                review.getContent(),
                address.getLocation(),
                point.getY(), // latitude
                point.getX(), // longitude
                mediaUrl
        );

    }

    private List<String> createCommunityPlaceMedia(CreateReviewDto createReviewDto, CommunityPlaceReview communityPlaceReview) {
        return communityPlaceMediaService.createCommunityPlaceMedia(communityPlaceReview, createReviewDto.getImages());

    }


}
