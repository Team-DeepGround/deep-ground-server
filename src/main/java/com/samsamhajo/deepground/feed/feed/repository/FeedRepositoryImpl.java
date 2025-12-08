package com.samsamhajo.deepground.feed.feed.repository;

import com.samsamhajo.deepground.feed.feed.model.v2.FetchFeedResponse;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FeedRepositoryImpl implements FeedRepositoryCustom {

    private final EntityManager em;

    @Override
    public Slice<FetchFeedResponse> findFeeds(Pageable pageable, Long memberId) {

        List<FetchFeedResponse> feeds = em.createQuery("select new com.samsamhajo.deepground.feed.feed.model.v2.FetchFeedResponseV2" +
                        "(m.publicId , m.memberProfile.profilePublicId , f.id, m.nickname, f.content, f.likeCount, f.commentCount," +
                        "f.sharedCount, m.memberProfile.profileImage, f.createdAt)" +
                        "from Feed f " +
                        "join f.member m", FetchFeedResponse.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize()+1)
                .getResultList();

        boolean hasNext = false;
        if (feeds.size() > pageable.getPageSize()) {
            feeds.remove(pageable.getPageSize());
            hasNext = true;
        }
        return new SliceImpl<>(feeds,pageable,hasNext);
    }
}
