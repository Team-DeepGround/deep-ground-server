package com.samsamhajo.deepground.feed.feed.service;

import com.samsamhajo.deepground.feed.feed.entity.Feed;
import com.samsamhajo.deepground.feed.feed.entity.FeedMedia;
import com.samsamhajo.deepground.feed.feed.model.FeedMediaResponse;
import com.samsamhajo.deepground.feed.feed.model.FeedUpdateRequest;
import com.samsamhajo.deepground.feed.feed.repository.FeedMediaRepository;
import com.samsamhajo.deepground.media.MediaErrorCode;
import com.samsamhajo.deepground.media.MediaException;
import com.samsamhajo.deepground.media.MediaUtils;
import com.samsamhajo.deepground.member.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeedMediaServiceTest {

    @Mock
    private FeedMediaRepository feedMediaRepository;

    @InjectMocks
    private FeedMediaService feedMediaService;

    private static final String TEST_CONTENT = "테스트 피드 내용입니다.";
    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "password123";
    private static final String TEST_NICKNAME = "테스트유저";
    private static final String TEST_MEDIA_URL = "test/url/image.jpg";
    private static final String TEST_EXTENSION = "jpg";

    private Member testMember;
    private Feed testFeed;
    private FeedMedia testFeedMedia;
    private MockMultipartFile testImage;

    @BeforeEach
    void setUp() {
        testMember = Member.createLocalMember(TEST_EMAIL, TEST_PASSWORD, TEST_NICKNAME);
        testFeed = Feed.of(TEST_CONTENT, testMember);
        testFeedMedia = FeedMedia.of(TEST_MEDIA_URL, TEST_EXTENSION, testFeed);
        testImage = new MockMultipartFile(
                "image",
                "test.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );

        ReflectionTestUtils.setField(testMember, "id", 1L);
        ReflectionTestUtils.setField(testFeed, "id", 1L);
        ReflectionTestUtils.setField(testFeedMedia, "id", 1L);
    }

    @Test
    @DisplayName("피드 미디어 생성 성공")
    void createFeedMediaSuccess() {
        // given
        try (MockedStatic<MediaUtils> mediaUtils = mockStatic(MediaUtils.class)) {
            mediaUtils.when(() -> MediaUtils.generateMediaUrl(any(MultipartFile.class)))
                    .thenReturn(TEST_MEDIA_URL);
            mediaUtils.when(() -> MediaUtils.getExtension(any(MultipartFile.class)))
                    .thenReturn(TEST_EXTENSION);

            when(feedMediaRepository.saveAll(anyList())).thenReturn(List.of(testFeedMedia));

            // when
            feedMediaService.createFeedMedia(testFeed, List.of(testImage));

            // then
            verify(feedMediaRepository).saveAll(anyList());
        }
    }

    @Test
    @DisplayName("피드 미디어 생성 - 이미지가 없는 경우")
    void createFeedMediaWithEmptyImages() {
        // when
        feedMediaService.createFeedMedia(testFeed, List.of());

        // then
        verify(feedMediaRepository, never()).saveAll(anyList());
    }

    @Test
    @DisplayName("피드 미디어 조회 성공")
    void fetchFeedMediaSuccess() {
        // given
        try (MockedStatic<MediaUtils> mediaUtils = mockStatic(MediaUtils.class)) {
            InputStreamResource mockResource = mock(InputStreamResource.class);
            mediaUtils.when(() -> MediaUtils.getMedia(TEST_MEDIA_URL))
                    .thenReturn(mockResource);

            when(feedMediaRepository.getById(1L)).thenReturn(testFeedMedia);

            // when
            FeedMediaResponse response = feedMediaService.fetchFeedMedia(1L);

            // then
            assertThat(response).isNotNull();
            assertThat(response.getImage()).isEqualTo(mockResource);
            assertThat(response.getExtension()).isEqualTo(TEST_EXTENSION);
        }
    }

    @Test
    @DisplayName("피드 미디어 조회 실패 - 존재하지 않는 미디어")
    void fetchFeedMediaFailWithNotFound() {
        // given
        when(feedMediaRepository.getById(1L))
                .thenThrow(new MediaException(MediaErrorCode.MEDIA_NOT_FOUND, 1L));

        // when & then
        assertThatThrownBy(() -> feedMediaService.fetchFeedMedia(1L))
                .isInstanceOf(MediaException.class)
                .hasFieldOrPropertyWithValue("errorCode", MediaErrorCode.MEDIA_NOT_FOUND);
    }

    @Test
    @DisplayName("피드의 모든 미디어 조회 성공")
    void findAllByFeedSuccess() {
        // given
        when(feedMediaRepository.findAllByFeedId(1L)).thenReturn(List.of(testFeedMedia));

        // when
        List<FeedMedia> result = feedMediaService.findAllByFeed(testFeed);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getMediaUrl()).isEqualTo(TEST_MEDIA_URL);
    }

    @Test
    @DisplayName("피드의 모든 미디어 삭제 성공")
    void deleteAllByFeedIdSuccess() {
        // given
        try (MockedStatic<MediaUtils> mediaUtils = mockStatic(MediaUtils.class)) {
            when(feedMediaRepository.findAllByFeedId(1L)).thenReturn(List.of(testFeedMedia));

            // when
            feedMediaService.deleteAllByFeedId(1L);

            // then
            mediaUtils.verify(() -> MediaUtils.deleteMedia(TEST_MEDIA_URL));
            verify(feedMediaRepository).deleteAllByFeedId(1L);
        }
    }

    @Test
    @DisplayName("피드 미디어 업데이트 성공")
    void updateFeedMediaSuccess() {
        // given
        try (MockedStatic<MediaUtils> mediaUtils = mockStatic(MediaUtils.class)) {
            mediaUtils.when(() -> MediaUtils.generateMediaUrl(any(MultipartFile.class)))
                    .thenReturn(TEST_MEDIA_URL);
            mediaUtils.when(() -> MediaUtils.getExtension(any(MultipartFile.class)))
                    .thenReturn(TEST_EXTENSION);

            when(feedMediaRepository.findAllByFeedId(1L)).thenReturn(List.of(testFeedMedia));
            when(feedMediaRepository.saveAll(anyList())).thenReturn(List.of(testFeedMedia));

            FeedUpdateRequest updateRequest = new FeedUpdateRequest(TEST_CONTENT, List.of(testImage));

            // when
            feedMediaService.updateFeedMedia(testFeed, updateRequest);

            // then
            mediaUtils.verify(() -> MediaUtils.deleteMedia(TEST_MEDIA_URL));
            verify(feedMediaRepository).deleteAllByFeedId(1L);
            verify(feedMediaRepository).saveAll(anyList());
        }
    }

    @Test
    @DisplayName("피드 미디어 ID 목록 조회 성공")
    void findAllMediaIdsByFeedIdSuccess() {
        // given
        when(feedMediaRepository.findAllByFeedId(1L)).thenReturn(List.of(testFeedMedia));

        // when
        List<Long> result = feedMediaService.findAllMediaIdsByFeedId(1L);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(1L);
    }
} 