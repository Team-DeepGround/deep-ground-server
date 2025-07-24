package com.samsamhajo.deepground.communityPlace.service;

import com.samsamhajo.deepground.communityPlace.entity.CommunityPlaceMedia;
import com.samsamhajo.deepground.communityPlace.entity.CommunityPlaceReview;
import com.samsamhajo.deepground.communityPlace.repository.CommunityPlaceMediaRepository;
import com.samsamhajo.deepground.global.upload.S3Uploader;
import com.samsamhajo.deepground.media.MediaUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.samsamhajo.deepground.media.MediaUtils.getExtension;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CommunityPlaceMediaService {

    private final S3Uploader s3Uploader;
    private final CommunityPlaceMediaRepository communityPlaceMediaRepository;

    public List<String> createCommunityPlaceMedia(CommunityPlaceReview communityPlaceReview, List<MultipartFile> images) {
        if (images == null || images.isEmpty()) {
            return Collections.emptyList();
        }

        List<CommunityPlaceMedia> mediaEntities = images.stream()
                .map(image -> {
                    String url = s3Uploader.upload(image, "question-media");
                    String extension = getExtension(image.getOriginalFilename());
                    return CommunityPlaceMedia.of(url, extension, communityPlaceReview);
                })
                .collect(Collectors.toList());

        communityPlaceMediaRepository.saveAll(mediaEntities);
        return mediaEntities.stream().map(CommunityPlaceMedia::getMediaUrl).toList();
    }

    /**
     * 후에 Review 수정, 삭제 기능이 추가된다면 사용 할 일이 있을 예정
     */
    public void deleteCommunityPlaceMedia(Long communityPlaceReviewId) {
        List<CommunityPlaceMedia> communityPlaceMediaList = communityPlaceMediaRepository.findAllByCommunityPlaceReviewId(communityPlaceReviewId);

        if (communityPlaceMediaList.isEmpty()) {
            return;
        }

        communityPlaceMediaList.forEach(media -> {
            try {
                MediaUtils.deleteMedia(media.getMediaUrl());
            } catch (Exception e) {
                log.warn("미디어 파일 삭제 실패: {}", media.getMediaUrl(), e);
            }
        });
        communityPlaceMediaRepository.deleteAllByCommunityPlaceReviewId(communityPlaceReviewId);
    }


}
