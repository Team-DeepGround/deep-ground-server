package com.samsamhajo.deepground.feed.feedcomment.service;

import com.samsamhajo.deepground.feed.feedcomment.entity.FeedComment;
import com.samsamhajo.deepground.feed.feedcomment.entity.FeedCommentMedia;
import com.samsamhajo.deepground.feed.feedcomment.model.FeedCommentMediaResponse;
import com.samsamhajo.deepground.feed.feedcomment.repository.FeedCommentMediaRepository;
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
class FeedCommentMediaServiceTest {

    @InjectMocks
    private FeedCommentMediaService feedCommentMediaService;

    @Mock
    private FeedCommentMediaRepository feedCommentMediaRepository;

    @Test
    @DisplayName("피드 댓글 미디어 조회 성공 테스트")
    void fetchFeedCommentMedia_Success() {
        // given
        Long mediaId = 1L;
        FeedCommentMedia feedCommentMedia = mock(FeedCommentMedia.class);
        when(feedCommentMedia.getMediaUrl()).thenReturn("test.jpg");
        when(feedCommentMedia.getExtension()).thenReturn("jpg");

        when(feedCommentMediaRepository.findById(mediaId)).thenReturn(java.util.Optional.of(feedCommentMedia));

        // when
        FeedCommentMediaResponse response = feedCommentMediaService.fetchFeedCommentMedia(mediaId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getExtension()).isEqualTo("jpg");
    }

    @Test
    @DisplayName("피드 댓글 미디어 조회 실패 테스트 - 존재하지 않는 미디어")
    void fetchFeedCommentMedia_Fail_MediaNotFound() {
        // given
        Long nonExistentMediaId = 999L;

        when(feedCommentMediaRepository.findById(nonExistentMediaId))
            .thenReturn(java.util.Optional.empty());

        // when & then
        assertThatThrownBy(() -> feedCommentMediaService.fetchFeedCommentMedia(nonExistentMediaId))
            .isInstanceOf(MediaException.class)
            .hasFieldOrPropertyWithValue("errorCode", MediaErrorCode.MEDIA_NOT_FOUND);
    }

    @Test
    @DisplayName("피드 댓글 미디어 생성 성공 테스트")
    void createFeedCommentMedia_Success() {
        // given
        FeedComment feedComment = mock(FeedComment.class);
        MockMultipartFile image = new MockMultipartFile(
            "image",
            "test.jpg",
            "image/jpeg",
            "테스트 이미지 데이터".getBytes()
        );

        // when
        feedCommentMediaService.createFeedCommentMedia(feedComment, List.of(image));

        // then
        verify(feedCommentMediaRepository).saveAll(any());
    }

    @Test
    @DisplayName("피드 댓글 미디어 삭제 성공 테스트")
    void deleteAllByFeedCommentId_Success() {
        // given
        Long feedCommentId = 1L;
        List<FeedCommentMedia> mediaList = List.of(
            mock(FeedCommentMedia.class),
            mock(FeedCommentMedia.class)
        );

        when(feedCommentMediaRepository.findAllByFeedCommentId(feedCommentId)).thenReturn(mediaList);

        // when
        feedCommentMediaService.deleteAllByFeedCommentId(feedCommentId);

        // then
        verify(feedCommentMediaRepository).deleteAllByFeedCommentId(feedCommentId);
    }
} 