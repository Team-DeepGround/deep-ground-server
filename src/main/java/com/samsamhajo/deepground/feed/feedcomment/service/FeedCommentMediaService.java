package com.samsamhajo.deepground.feed.feedcomment.service;

import com.samsamhajo.deepground.feed.feedcomment.entity.FeedComment;
import com.samsamhajo.deepground.feed.feedcomment.entity.FeedCommentMedia;
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
        if(CollectionUtils.isEmpty(images)) return;

        feedCommentMediaRepository.saveAll(
                images.stream()
                        .map(image -> FeedCommentMedia.of(
                                MediaUtils.generateMediaUrl(image),
                                MediaUtils.getExtension(image),
                                feedComment))
                        .toList()
        );
    }
} 