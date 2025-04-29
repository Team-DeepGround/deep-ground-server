package com.samsamhajo.deepground.feed.feedreply;


import com.samsamhajo.deepground.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FeedReplyMedia extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_reply_media_id")
    private Long id;

    @Column(length = 1024)
    private String media_url;

    @Column
    private String extension;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private FeedReply feed;
}
