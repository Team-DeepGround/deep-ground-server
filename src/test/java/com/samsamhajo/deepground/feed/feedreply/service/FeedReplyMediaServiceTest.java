package com.samsamhajo.deepground.feed.feedreply.service;

import com.samsamhajo.deepground.feed.feedreply.entity.FeedReply;
import com.samsamhajo.deepground.feed.feedreply.entity.FeedReplyMedia;
import com.samsamhajo.deepground.feed.feedreply.model.FeedReplyMediaResponse;
import com.samsamhajo.deepground.feed.feedreply.repository.FeedReplyMediaRepository;
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
class FeedReplyMediaServiceTest {

    @InjectMocks
    private FeedReplyMediaService feedReplyMediaService;

    @Mock
    private FeedReplyMediaRepository feedReplyMediaRepository;

    @Test
    @DisplayName("피드 답글 미디어 조회 성공 테스트")
    void fetchFeedReplyMedia_Success() {
        // given
        Long mediaId = 1L;
        FeedReplyMedia feedReplyMedia = mock(FeedReplyMedia.class);
        when(feedReplyMedia.getMediaUrl()).thenReturn("test.jpg");
        when(feedReplyMedia.getExtension()).thenReturn("jpg");

        when(feedReplyMediaRepository.findById(mediaId)).thenReturn(java.util.Optional.of(feedReplyMedia));

        // when
        FeedReplyMediaResponse response = feedReplyMediaService.fetchFeedReplyMedia(mediaId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getExtension()).isEqualTo("jpg");
    }

    @Test
    @DisplayName("피드 답글 미디어 조회 실패 테스트 - 존재하지 않는 미디어")
    void fetchFeedReplyMedia_Fail_MediaNotFound() {
        // given
        Long nonExistentMediaId = 999L;

        when(feedReplyMediaRepository.findById(nonExistentMediaId))
            .thenReturn(java.util.Optional.empty());

        // when & then
        assertThatThrownBy(() -> feedReplyMediaService.fetchFeedReplyMedia(nonExistentMediaId))
            .isInstanceOf(MediaException.class)
            .hasFieldOrPropertyWithValue("errorCode", MediaErrorCode.MEDIA_NOT_FOUND);
    }

    @Test
    @DisplayName("피드 답글 미디어 생성 성공 테스트")
    void createFeedReplyMedia_Success() {
        // given
        FeedReply feedReply = mock(FeedReply.class);
        MockMultipartFile image = new MockMultipartFile(
            "image",
            "test.jpg",
            "image/jpeg",
            "테스트 이미지 데이터".getBytes()
        );

        // when
        feedReplyMediaService.createFeedReplyMedia(feedReply, List.of(image));

        // then
        verify(feedReplyMediaRepository).saveAll(any());
    }

    @Test
    @DisplayName("피드 답글 미디어 삭제 성공 테스트")
    void deleteAllByFeedReplyId_Success() {
        // given
        Long feedReplyId = 1L;
        List<FeedReplyMedia> mediaList = List.of(
            mock(FeedReplyMedia.class),
            mock(FeedReplyMedia.class)
        );

        when(feedReplyMediaRepository.findAllByFeedReplyId(feedReplyId)).thenReturn(mediaList);

        // when
        feedReplyMediaService.deleteAllByFeedReplyId(feedReplyId);

        // then
        verify(feedReplyMediaRepository).deleteAllByFeedReplyId(feedReplyId);
    }
} 