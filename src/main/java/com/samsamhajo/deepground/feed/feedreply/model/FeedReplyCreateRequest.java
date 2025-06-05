package com.samsamhajo.deepground.feed.feedreply.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@AllArgsConstructor
public class FeedReplyCreateRequest {
    private Long feedCommentId;
    private String content;
    private List<MultipartFile> images;
} 