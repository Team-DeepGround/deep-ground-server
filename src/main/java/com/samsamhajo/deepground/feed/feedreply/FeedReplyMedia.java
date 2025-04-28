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
    public Long id;

    @Column(length = 1024)
    public String media_url;

    @Column
    public String extension;

    @ManyToOne
    @JoinColumn(name = "feed_id")
    public FeedReply feed;
}
