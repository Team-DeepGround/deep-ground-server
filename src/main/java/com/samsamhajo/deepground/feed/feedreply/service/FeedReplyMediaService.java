package com.samsamhajo.deepground.feed.feedreply.service;

import com.samsamhajo.deepground.feed.feedreply.entity.FeedReply;
import com.samsamhajo.deepground.feed.feedreply.entity.FeedReplyMedia;
import com.samsamhajo.deepground.feed.feedreply.model.FeedReplyMediaResponse;
import com.samsamhajo.deepground.feed.feedreply.repository.FeedReplyMediaRepository;
import com.samsamhajo.deepground.media.MediaErrorCode;
import com.samsamhajo.deepground.media.MediaException;
import com.samsamhajo.deepground.media.MediaUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeedReplyMediaService {

    private final FeedReplyMediaRepository feedReplyMediaRepository;

    @Transactional
    public void createFeedReplyMedia(FeedReply feedReply, List<MultipartFile> images) {
        if (CollectionUtils.isEmpty(images)) return;

        feedReplyMediaRepository.saveAll(
                images.stream()
                        .map(image -> FeedReplyMedia.of(
                                MediaUtils.generateMediaUrl(image),
                                MediaUtils.getExtension(image),
                                feedReply))
                        .toList()
        );
    }

    @Transactional
    public void deleteAllByFeedReplyId(Long feedReplyId) {
        // 파일 시스템에서 물리적 미디어 파일 삭제
        List<FeedReplyMedia> mediaList = feedReplyMediaRepository.findAllByFeedReplyId(feedReplyId);
        for (FeedReplyMedia media : mediaList) {
            MediaUtils.deleteMedia(media.getMediaUrl());
        }

        // DB에서 삭제 (JPA Query Method 사용)
        feedReplyMediaRepository.deleteAllByFeedReplyId(feedReplyId);
    }

    @Transactional
    public void updateFeedReplyMedia(FeedReply feedReply, List<MultipartFile> images) {
        if (CollectionUtils.isEmpty(images)) return;

        // 피드에 연결된 모든 미디어 삭제
        deleteAllByFeedReplyId(feedReply.getId());

        // 새 미디어 추가
        createFeedReplyMedia(feedReply, images);
    }

    public List<Long> getFeedReplyMediaIds(Long feedReplyId) {
        return feedReplyMediaRepository.findAllByFeedReplyId(feedReplyId)
                .stream()
                .map(FeedReplyMedia::getId)
                .toList();
    }

    public FeedReplyMediaResponse fetchFeedReplyMedia(Long mediaId) {
        FeedReplyMedia media = feedReplyMediaRepository.findById(mediaId)
                .orElseThrow(() -> new MediaException(MediaErrorCode.MEDIA_NOT_FOUND));

        InputStreamResource image = MediaUtils.getMedia(media.getMediaUrl());
        return FeedReplyMediaResponse.of(image, media.getExtension());
    }
} 