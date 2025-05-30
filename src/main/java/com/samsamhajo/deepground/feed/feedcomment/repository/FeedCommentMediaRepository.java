package com.samsamhajo.deepground.feed.feedcomment.repository;

import com.samsamhajo.deepground.feed.feedcomment.entity.FeedCommentMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedCommentMediaRepository extends JpaRepository<FeedCommentMedia, Long> {

}