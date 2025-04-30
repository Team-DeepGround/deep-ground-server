package com.samsamhajo.deepground.feed;


import com.samsamhajo.deepground.global.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "feed_media")
@Getter
public class FeedMedia extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_media_id")
    private Long id;

    @Column(length = 1024, nullable = false)
    private String mediaUrl;

    @Column(length = 8, nullable = false)
    private String extension;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;

    protected FeedMedia() {
    }

    private FeedMedia(String mediaUrl, String extension, Feed feed) {
        this.mediaUrl = mediaUrl;
        this.extension = extension;
        this.feed = feed;
    }

    public static FeedMedia of(String mediaUrl, String extension, Feed feed) {
        return new FeedMedia(mediaUrl, extension, feed);
    }
}
