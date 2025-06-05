package com.samsamhajo.deepground.feed.feedcomment.service;

import com.samsamhajo.deepground.feed.feedcomment.entity.FeedComment;
import com.samsamhajo.deepground.feed.feedcomment.entity.FeedCommentMedia;
import com.samsamhajo.deepground.feed.feedcomment.model.FeedCommentUpdateRequest;
import com.samsamhajo.deepground.feed.feedcomment.repository.FeedCommentMediaRepository;
import com.samsamhajo.deepground.media.MediaUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeedCommentMediaService {

    private final FeedCommentMediaRepository feedCommentMediaRepository;

    @Transactional
    public void createFeedCommentMedia(FeedComment feedComment, List<MultipartFile> images) {
        if (CollectionUtils.isEmpty(images)) return;

        feedCommentMediaRepository.saveAll(
                images.stream()
                        .map(image -> FeedCommentMedia.of(
                                MediaUtils.generateMediaUrl(image),
                                MediaUtils.getExtension(image),
                                feedComment))
                        .toList()
        );
    }

    public void updateFeedCommentMedia(FeedComment feedComment, List<MultipartFile> images) {
        if (CollectionUtils.isEmpty(images)) return;

        // 피드 댓글에에 연결된 모든 미디어 삭제
        deleteAllByFeedCommentId(feedComment.getId());

        // 새 미디어 추가
        createFeedCommentMedia(feedComment, images);
    }

    @Transactional
    public void deleteAllByFeedCommentId(Long feedCommentId) {
        // 파일 시스템에서 물리적 미디어 파일 삭제
        List<FeedCommentMedia> mediaList = feedCommentMediaRepository.findAllByFeedCommentId(feedCommentId);

        for (FeedCommentMedia media : mediaList) {
            MediaUtils.deleteMedia(media.getMediaUrl());
        }

        // DB에서 삭제 (JPA Query Method 사용)
        feedCommentMediaRepository.deleteAllByFeedCommentId(feedCommentId);
    }

} 