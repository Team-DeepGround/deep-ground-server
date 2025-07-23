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

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.samsamhajo.deepground.media.MediaUtils.getExtension;

@Service
@RequiredArgsConstructor
@Transactional
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

    public void deleteCommunityPlaceMedia(Long communityPlaceReviewId) {
        List<CommunityPlaceMedia> communityPlaceMediaList = communityPlaceMediaRepository.findAllByCommunityPlaceReviewId(communityPlaceReviewId);

        if (communityPlaceMediaList.isEmpty()) {
            return;
        }

        communityPlaceMediaList.forEach(media -> {
            try {
                MediaUtils.deleteMedia(media.getMediaUrl());
            } catch (Exception e) {
            }
        });
        communityPlaceMediaRepository.deleteAllByCommunityPlaceReviewId(communityPlaceReviewId);
    }


}
