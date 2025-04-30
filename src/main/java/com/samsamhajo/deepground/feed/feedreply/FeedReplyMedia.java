package com.samsamhajo.deepground.feed.feedreply;


import com.samsamhajo.deepground.global.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "feed_reply_media")
@Getter
public class FeedReplyMedia extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_reply_media_id")
    private Long id;

    @Column(length = 1024, nullable = false)
    private String mediaUrl;

    @Column(length = 8, nullable = false)
    private String extension;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private FeedReply feedReply;

    protected FeedReplyMedia() {
    }

    private FeedReplyMedia(String mediaUrl, String extension, FeedReply feedReply) {
        this.mediaUrl = mediaUrl;
        this.extension = extension;
        this.feedReply = feedReply;
    }

    public static FeedReplyMedia of(String mediaUrl, String extension, FeedReply feedReply) {
        return new FeedReplyMedia(mediaUrl, extension, feedReply);
    }
}
