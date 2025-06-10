package com.samsamhajo.deepground.feed.feed.service;

import com.samsamhajo.deepground.feed.feed.entity.Feed;
import com.samsamhajo.deepground.feed.feed.entity.FeedMedia;
import com.samsamhajo.deepground.feed.feed.model.FeedMediaResponse;
import com.samsamhajo.deepground.feed.feed.repository.FeedMediaRepository;
import com.samsamhajo.deepground.media.MediaErrorCode;
import com.samsamhajo.deepground.media.MediaException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeedMediaServiceTest {

    @InjectMocks
    private FeedMediaService feedMediaService;

    @Mock
    private FeedMediaRepository feedMediaRepository;

    @Test
    @DisplayName("피드 미디어 조회 성공 테스트")
    void fetchFeedMedia_Success() {
        // given
        Long mediaId = 1L;
        FeedMedia feedMedia = mock(FeedMedia.class);
        when(feedMedia.getMediaUrl()).thenReturn("test.jpg");
        when(feedMedia.getExtension()).thenReturn("jpg");

        when(feedMediaRepository.getById(mediaId)).thenReturn(feedMedia);

        // when
        FeedMediaResponse response = feedMediaService.fetchFeedMedia(mediaId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getExtension()).isEqualTo("jpg");
    }

    @Test
    @DisplayName("피드 미디어 조회 실패 테스트 - 존재하지 않는 미디어")
    void fetchFeedMedia_Fail_MediaNotFound() {
        // given
        Long nonExistentMediaId = 999L;

        when(feedMediaRepository.getById(nonExistentMediaId))
            .thenThrow(new MediaException(MediaErrorCode.MEDIA_NOT_FOUND));

        // when & then
        assertThatThrownBy(() -> feedMediaService.fetchFeedMedia(nonExistentMediaId))
            .isInstanceOf(MediaException.class)
            .hasFieldOrPropertyWithValue("errorCode", MediaErrorCode.MEDIA_NOT_FOUND);
    }

    @Test
    @DisplayName("피드 미디어 생성 성공 테스트")
    void createFeedMedia_Success() {
        // given
        Feed feed = mock(Feed.class);
        MockMultipartFile image = new MockMultipartFile(
            "image",
            "test.jpg",
            "image/jpeg",
            "테스트 이미지 데이터".getBytes()
        );

        // when
        feedMediaService.createFeedMedia(feed, List.of(image));

        // then
        verify(feedMediaRepository).saveAll(any());
    }

    @Test
    @DisplayName("피드 미디어 삭제 성공 테스트")
    void deleteAllByFeedId_Success() {
        // given
        Long feedId = 1L;
        List<FeedMedia> mediaList = List.of(
            mock(FeedMedia.class),
            mock(FeedMedia.class)
        );

        when(feedMediaRepository.findAllByFeedId(feedId)).thenReturn(mediaList);

        // when
        feedMediaService.deleteAllByFeedId(feedId);

        // then
        verify(feedMediaRepository).deleteAllByFeedId(feedId);
    }
} 