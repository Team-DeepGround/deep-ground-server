package com.samsamhajo.deepground.feed.feedcomment;


import com.samsamhajo.deepground.global.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "feed_comment_media")
@Getter
public class FeedCommentMedia extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_comment_media_id")
    private Long id;

    @Column(length = 1024, nullable = false)
    private String mediaUrl;

    @Column(length = 8, nullable = false)
    private String extension;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private FeedComment feedComment;


    protected FeedCommentMedia() {
    }

    private FeedCommentMedia(String mediaUrl, String extension, FeedComment feedComment) {
        this.mediaUrl = mediaUrl;
        this.extension = extension;
        this.feedComment = feedComment;
    }

    public static FeedCommentMedia of(String mediaUrl, String extension, FeedComment feedComment) {
        return new FeedCommentMedia(mediaUrl, extension, feedComment);
    }
}
