package com.samsamhajo.deepground.feed.feedreply.service;

import com.samsamhajo.deepground.feed.feedcomment.entity.FeedComment;
import com.samsamhajo.deepground.feed.feedreply.entity.FeedReply;
import com.samsamhajo.deepground.feed.feedreply.entity.FeedReplyMedia;
import com.samsamhajo.deepground.feed.feedreply.model.FeedReplyMediaResponse;
import com.samsamhajo.deepground.feed.feedreply.repository.FeedReplyMediaRepository;
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
class FeedReplyMediaServiceTest {

    @Mock
    private FeedReplyMediaRepository feedReplyMediaRepository;

    @InjectMocks
    private FeedReplyMediaService feedReplyMediaService;

    private static final String TEST_CONTENT = "테스트 답글 내용입니다.";
    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "password123";
    private static final String TEST_NICKNAME = "테스트유저";
    private static final String TEST_MEDIA_URL = "test.jpg";
    private static final String TEST_EXTENSION = "jpg";

    private Member testMember;
    private FeedComment testFeedComment;
    private FeedReply testFeedReply;
    private FeedReplyMedia testFeedReplyMedia;
    private MockMultipartFile testImage;
    private InputStreamResource mockResource;

    @BeforeEach
    void setUp() throws IOException {
        testMember = Member.createLocalMember(TEST_EMAIL, TEST_PASSWORD, TEST_NICKNAME);
        testFeedComment = FeedComment.of(TEST_CONTENT, null, testMember);
        testFeedReply = FeedReply.of(TEST_CONTENT, testFeedComment, testMember);
        testFeedReplyMedia = FeedReplyMedia.of(TEST_MEDIA_URL, TEST_EXTENSION, testFeedReply);
        testImage = new MockMultipartFile(
                "image",
                "test.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );
        mockResource = new InputStreamResource(testImage.getInputStream());

        ReflectionTestUtils.setField(testMember, "id", 1L);
        ReflectionTestUtils.setField(testFeedComment, "id", 1L);
        ReflectionTestUtils.setField(testFeedReply, "id", 1L);
        ReflectionTestUtils.setField(testFeedReplyMedia, "id", 1L);
    }

    @Test
    @DisplayName("피드 답글 미디어 생성 성공")
    void createFeedReplyMediaSuccess() {
        // given
        try (MockedStatic<MediaUtils> mediaUtils = mockStatic(MediaUtils.class)) {
            mediaUtils.when(() -> MediaUtils.generateMediaUrl(any(MultipartFile.class)))
                    .thenReturn(TEST_MEDIA_URL);
            mediaUtils.when(() -> MediaUtils.getExtension(any(MultipartFile.class)))
                    .thenReturn(TEST_EXTENSION);

            when(feedReplyMediaRepository.saveAll(anyList())).thenReturn(List.of(testFeedReplyMedia));

            // when
            feedReplyMediaService.createFeedReplyMedia(testFeedReply, List.of(testImage));

            // then
            verify(feedReplyMediaRepository).saveAll(anyList());
        }
    }

    @Test
    @DisplayName("피드 답글 미디어 생성 성공 - 이미지가 없는 경우")
    void createFeedReplyMediaSuccessWithNoImages() {
        // when
        feedReplyMediaService.createFeedReplyMedia(testFeedReply, List.of());

        // then
        verify(feedReplyMediaRepository, never()).saveAll(anyList());
    }

    @Test
    @DisplayName("피드 답글 미디어 조회 성공")
    void fetchFeedReplyMediaSuccess() {
        // given
        try (MockedStatic<MediaUtils> mediaUtils = mockStatic(MediaUtils.class)) {
            mediaUtils.when(() -> MediaUtils.getMedia(anyString())).thenReturn(mockResource);

            when(feedReplyMediaRepository.findById(1L)).thenReturn(java.util.Optional.of(testFeedReplyMedia));

            // when
            FeedReplyMediaResponse response = feedReplyMediaService.fetchFeedReplyMedia(1L);

            // then
            assertThat(response).isNotNull();
            assertThat(response.getImage()).isEqualTo(mockResource);
            assertThat(response.getExtension()).isEqualTo(TEST_EXTENSION);
        }
    }

    @Test
    @DisplayName("피드 답글 미디어 조회 실패 - 미디어가 존재하지 않는 경우")
    void fetchFeedReplyMediaFailWithNotFound() {
        // given
        when(feedReplyMediaRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        // when & then
        assertThatThrownBy(() -> feedReplyMediaService.fetchFeedReplyMedia(1L))
                .isInstanceOf(MediaException.class)
                .hasFieldOrPropertyWithValue("errorCode", MediaErrorCode.MEDIA_NOT_FOUND);
    }

    @Test
    @DisplayName("피드 답글 미디어 삭제 성공")
    void deleteAllByFeedReplyIdSuccess() {
        // given
        try (MockedStatic<MediaUtils> mediaUtils = mockStatic(MediaUtils.class)) {
            when(feedReplyMediaRepository.findAllByFeedReplyId(1L))
                    .thenReturn(List.of(testFeedReplyMedia));

            // when
            feedReplyMediaService.deleteAllByFeedReplyId(1L);

            // then
            mediaUtils.verify(() -> MediaUtils.deleteMedia(anyString()));
            verify(feedReplyMediaRepository).deleteAllByFeedReplyId(1L);
        }
    }

    @Test
    @DisplayName("피드 답글 미디어 ID 목록 조회 성공")
    void getFeedReplyMediaIdsSuccess() {
        // given
        when(feedReplyMediaRepository.findAllByFeedReplyId(1L))
                .thenReturn(List.of(testFeedReplyMedia));

        // when
        List<Long> mediaIds = feedReplyMediaService.getFeedReplyMediaIds(1L);

        // then
        assertThat(mediaIds).hasSize(1);
        assertThat(mediaIds.get(0)).isEqualTo(1L);
    }

    @Test
    @DisplayName("피드 답글 미디어 업데이트 성공")
    void updateFeedReplyMediaSuccess() {
        // given
        try (MockedStatic<MediaUtils> mediaUtils = mockStatic(MediaUtils.class)) {
            mediaUtils.when(() -> MediaUtils.generateMediaUrl(any(MultipartFile.class)))
                    .thenReturn(TEST_MEDIA_URL);
            mediaUtils.when(() -> MediaUtils.getExtension(any(MultipartFile.class)))
                    .thenReturn(TEST_EXTENSION);

            when(feedReplyMediaRepository.findAllByFeedReplyId(1L))
                    .thenReturn(List.of(testFeedReplyMedia));
            when(feedReplyMediaRepository.saveAll(anyList()))
                    .thenReturn(List.of(testFeedReplyMedia));

            // when
            feedReplyMediaService.updateFeedReplyMedia(testFeedReply, List.of(testImage));

            // then
            mediaUtils.verify(() -> MediaUtils.deleteMedia(anyString()));
            verify(feedReplyMediaRepository).deleteAllByFeedReplyId(1L);
            verify(feedReplyMediaRepository).saveAll(anyList());
        }
    }
} 