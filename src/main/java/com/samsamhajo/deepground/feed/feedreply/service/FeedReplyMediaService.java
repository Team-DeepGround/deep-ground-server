package com.samsamhajo.deepground.feed.feedreply.service;

import com.samsamhajo.deepground.feed.feedreply.entity.FeedReply;
import com.samsamhajo.deepground.feed.feedreply.entity.FeedReplyMedia;
import com.samsamhajo.deepground.feed.feedreply.repository.FeedReplyMediaRepository;
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
    public void updateFeedReplyMedia(FeedReply feedReply, List<MultipartFile> images) {
        if (CollectionUtils.isEmpty(images)) return;

        deleteFeedReplyMedia(feedReply.getId());

        createFeedReplyMedia(feedReply, images);
    }

    @Transactional
    public void deleteFeedReplyMedia(Long feedReplyId) {
        feedReplyMediaRepository.deleteAllByFeedReplyId(feedReplyId);
    }
} 