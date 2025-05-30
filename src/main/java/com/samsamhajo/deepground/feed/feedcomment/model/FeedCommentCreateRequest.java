package com.samsamhajo.deepground.feed.feedcomment.model;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class FeedCommentCreateRequest {
    private Long feedId;
    private String content;
    private List<MultipartFile> images;
} 