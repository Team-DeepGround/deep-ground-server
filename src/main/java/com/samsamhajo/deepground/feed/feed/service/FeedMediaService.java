package com.samsamhajo.deepground.feed.feed.service;

import com.samsamhajo.deepground.feed.feed.entity.Feed;
import com.samsamhajo.deepground.feed.feed.entity.FeedMedia;
import com.samsamhajo.deepground.feed.feed.model.FeedMediaResponse;
import com.samsamhajo.deepground.feed.feed.model.FeedUpdateRequest;
import com.samsamhajo.deepground.feed.feed.repository.FeedMediaRepository;
import com.samsamhajo.deepground.global.upload.S3Uploader;
import com.samsamhajo.deepground.media.MediaUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.samsamhajo.deepground.media.MediaUtils.getExtension;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeedMediaService {

    private final FeedMediaRepository feedMediaRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    public void createFeedMedia(Feed feed, List<MultipartFile> images){
        if(CollectionUtils.isEmpty(images)) return;

        feedMediaRepository.saveAll(
                images.stream()
                        .map(image -> FeedMedia.of(
                                MediaUtils.generateMediaUrl(image),
                                MediaUtils.getExtension(image),
                                feed))
                        .toList()
        );
    }

    public FeedMediaResponse fetchFeedMedia(Long feedMediaId) {
        FeedMedia feedMedia = feedMediaRepository.getById(feedMediaId);
        InputStreamResource media = MediaUtils.getMedia(feedMedia.getMediaUrl());

        return FeedMediaResponse.of(media, feedMedia.getExtension());
    }

    public List<FeedMedia> findAllByFeed(Feed feed) {
        return feedMediaRepository.findAllByFeedId(feed.getId());
    }
    
    @Transactional
    public void deleteAllByFeedId(Long feedId) {
        // 파일 시스템에서 물리적 미디어 파일 삭제
        List<FeedMedia> mediaList = feedMediaRepository.findAllByFeedId(feedId);
        for (FeedMedia media : mediaList) {
            MediaUtils.deleteMedia(media.getMediaUrl());
        }
        
        // DB에서 삭제 (JPA Query Method 사용)
        feedMediaRepository.deleteAllByFeedId(feedId);
    }

    public void updateFeedMedia(Feed feed, FeedUpdateRequest request) {
        // 피드에 연결된 모든 미디어 삭제
        deleteAllByFeedId(feed.getId());

        // 새 미디어 추가
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            createFeedMedia(feed, request.getImages());
        }
    }

    public List<Long> findAllMediaIdsByFeedId(Long feedId) {
        return feedMediaRepository.findAllByFeedId(feedId)
                .stream()
                .map(FeedMedia::getId)
                .toList();
    }
}