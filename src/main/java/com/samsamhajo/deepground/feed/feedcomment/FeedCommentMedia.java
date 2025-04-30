package com.samsamhajo.deepground.feed.feedcomment;


import com.samsamhajo.deepground.feed.feedcomment.FeedComment;
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
public class FeedCommentMedia extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_comment_media_id")
    private Long id;

    @Column(length = 1024)
    private String mediaUrl;

    @Column
    private String extension;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    public FeedComment feed;
}
