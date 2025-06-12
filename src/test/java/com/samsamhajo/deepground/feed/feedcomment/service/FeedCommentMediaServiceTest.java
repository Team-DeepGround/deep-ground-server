package com.samsamhajo.deepground.feed.feedcomment.service;

import com.samsamhajo.deepground.feed.feed.entity.Feed;
import com.samsamhajo.deepground.feed.feedcomment.entity.FeedComment;
import com.samsamhajo.deepground.feed.feedcomment.entity.FeedCommentMedia;
import com.samsamhajo.deepground.feed.feedcomment.model.FeedCommentMediaResponse;
import com.samsamhajo.deepground.feed.feedcomment.repository.FeedCommentMediaRepository;
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

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeedCommentMediaServiceTest {

    @Mock
    private FeedCommentMediaRepository feedCommentMediaRepository;

    @InjectMocks
    private FeedCommentMediaService feedCommentMediaService;

    private static final String TEST_CONTENT = "테스트 댓글 내용입니다.";
    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "password123";
    private static final String TEST_NICKNAME = "테스트유저";
    private static final String TEST_MEDIA_URL = "test.jpg";
    private static final String TEST_EXTENSION = "jpg";

    private Member testMember;
    private Feed testFeed;
    private FeedComment testFeedComment;
    private FeedCommentMedia testFeedCommentMedia;
    private MockMultipartFile testImage;
    private InputStreamResource mockResource;

    @BeforeEach
    void setUp() throws IOException {
        testMember = Member.createLocalMember(TEST_EMAIL, TEST_PASSWORD, TEST_NICKNAME);
        testFeed = Feed.of(TEST_CONTENT, testMember);
        testFeedComment = FeedComment.of(TEST_CONTENT, testFeed, testMember);
        testFeedCommentMedia = FeedCommentMedia.of(TEST_MEDIA_URL, TEST_EXTENSION, testFeedComment);
        testImage = new MockMultipartFile(
                "image",
                "test.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );
        mockResource = new InputStreamResource(testImage.getInputStream());

        ReflectionTestUtils.setField(testMember, "id", 1L);
        ReflectionTestUtils.setField(testFeed, "id", 1L);
        ReflectionTestUtils.setField(testFeedComment, "id", 1L);
        ReflectionTestUtils.setField(testFeedCommentMedia, "id", 1L);
    }

    @Test
    @DisplayName("피드 댓글 미디어 생성 성공")
    void createFeedCommentMediaSuccess() {
        // given
        try (MockedStatic<MediaUtils> mediaUtils = mockStatic(MediaUtils.class)) {
            mediaUtils.when(() -> MediaUtils.generateMediaUrl(any(MultipartFile.class)))
                    .thenReturn(TEST_MEDIA_URL);
            mediaUtils.when(() -> MediaUtils.getExtension(any(MultipartFile.class)))
                    .thenReturn(TEST_EXTENSION);

            when(feedCommentMediaRepository.saveAll(anyList())).thenReturn(List.of(testFeedCommentMedia));

            // when
            feedCommentMediaService.createFeedCommentMedia(testFeedComment, List.of(testImage));

            // then
            verify(feedCommentMediaRepository).saveAll(anyList());
        }
    }

    @Test
    @DisplayName("피드 댓글 미디어 생성 성공 - 이미지가 없는 경우")
    void createFeedCommentMediaSuccessWithNoImages() {
        // when
        feedCommentMediaService.createFeedCommentMedia(testFeedComment, List.of());

        // then
        verify(feedCommentMediaRepository, never()).saveAll(anyList());
    }

    @Test
    @DisplayName("피드 댓글 미디어 조회 성공")
    void fetchFeedCommentMediaSuccess() {
        // given
        try (MockedStatic<MediaUtils> mediaUtils = mockStatic(MediaUtils.class)) {
            mediaUtils.when(() -> MediaUtils.getMedia(anyString())).thenReturn(mockResource);

            when(feedCommentMediaRepository.findById(1L)).thenReturn(java.util.Optional.of(testFeedCommentMedia));

            // when
            FeedCommentMediaResponse response = feedCommentMediaService.fetchFeedCommentMedia(1L);

            // then
            assertThat(response).isNotNull();
            assertThat(response.getImage()).isEqualTo(mockResource);
            assertThat(response.getExtension()).isEqualTo(TEST_EXTENSION);
        }
    }

    @Test
    @DisplayName("피드 댓글 미디어 조회 실패 - 미디어가 존재하지 않는 경우")
    void fetchFeedCommentMediaFailWithNotFound() {
        // given
        when(feedCommentMediaRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        // when & then
        assertThatThrownBy(() -> feedCommentMediaService.fetchFeedCommentMedia(1L))
                .isInstanceOf(MediaException.class)
                .hasFieldOrPropertyWithValue("errorCode", MediaErrorCode.MEDIA_NOT_FOUND);
    }

    @Test
    @DisplayName("피드 댓글 미디어 삭제 성공")
    void deleteAllByFeedCommentIdSuccess() {
        // given
        try (MockedStatic<MediaUtils> mediaUtils = mockStatic(MediaUtils.class)) {
            when(feedCommentMediaRepository.findAllByFeedCommentId(1L))
                    .thenReturn(List.of(testFeedCommentMedia));

            // when
            feedCommentMediaService.deleteAllByFeedCommentId(1L);

            // then
            mediaUtils.verify(() -> MediaUtils.deleteMedia(anyString()));
            verify(feedCommentMediaRepository).deleteAllByFeedCommentId(1L);
        }
    }

    @Test
    @DisplayName("피드 댓글 미디어 ID 목록 조회 성공")
    void getFeedCommentMediaIdsSuccess() {
        // given
        when(feedCommentMediaRepository.findAllByFeedCommentId(1L))
                .thenReturn(List.of(testFeedCommentMedia));

        // when
        List<Long> mediaIds = feedCommentMediaService.getFeedCommentMediaIds(1L);

        // then
        assertThat(mediaIds).hasSize(1);
        assertThat(mediaIds.get(0)).isEqualTo(1L);
    }

    @Test
    @DisplayName("피드 댓글 미디어 업데이트 성공")
    void updateFeedCommentMediaSuccess() {
        // given
        try (MockedStatic<MediaUtils> mediaUtils = mockStatic(MediaUtils.class)) {
            mediaUtils.when(() -> MediaUtils.generateMediaUrl(any(MultipartFile.class)))
                    .thenReturn(TEST_MEDIA_URL);
            mediaUtils.when(() -> MediaUtils.getExtension(any(MultipartFile.class)))
                    .thenReturn(TEST_EXTENSION);

            when(feedCommentMediaRepository.findAllByFeedCommentId(1L))
                    .thenReturn(List.of(testFeedCommentMedia));
            when(feedCommentMediaRepository.saveAll(anyList()))
                    .thenReturn(List.of(testFeedCommentMedia));

            // when
            feedCommentMediaService.updateFeedCommentMedia(testFeedComment, List.of(testImage));

            // then
            mediaUtils.verify(() -> MediaUtils.deleteMedia(anyString()));
            verify(feedCommentMediaRepository).deleteAllByFeedCommentId(1L);
            verify(feedCommentMediaRepository).saveAll(anyList());
        }
    }
} 