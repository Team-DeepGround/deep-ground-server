package com.samsamhajo.deepground.media;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.File;

@ExtendWith(MockitoExtension.class)
class MediaUtilsTest {

    @Mock
    private MultipartFile mockMultipartFile;

    @Test
    @DisplayName("파일이 존재하지 않을 때 미디어 파일 조회 시 예외가 발생한다")
    void getMedia_whenFileNotFound_thenThrowException() {
        // given
        String mediaUrl = "/dummy/path/not-exist.jpg";

        // when & then
        assertThatThrownBy(() -> MediaUtils.getMedia(mediaUrl))
                .isInstanceOf(MediaException.class);
    }

    @ParameterizedTest
    @CsvSource({
            "image.jpg, image/jpeg",
            "image.jpeg, image/jpeg",
            "image.png, image/png",
            "image.gif, image/gif",
            "document.pdf, application/octet-stream"
    })
    @DisplayName("파일 확장자에 따라 적절한 미디어 타입을 반환한다")
    void getMediaType_returnsCorrectMediaType(String filename, String expectedMimeType) {
        // given
        MediaType expected = MediaType.parseMediaType(expectedMimeType);

        // when
        MediaType actual = MediaUtils.getMediaType(filename);

        // then
        assertThat(actual).isEqualTo(expected);
    }


    @Test
    @DisplayName("멀티파트 파일로 미디어 URL을 생성한다")
    void generateMediaUrl_returnsValidUrl() {
        // given
        String mediaUrl = MediaUtils.generateMediaUrl(mockMultipartFile);

        // then
        assertThat(mediaUrl).startsWith("/media/");
    }

    @Test
    @DisplayName("파일명에서 확장자를 추출한다")
    void getExtension_fromFilename_returnsCorrectExtension() {
        // given
        String filename = "image.jpg";

        // when
        String extension = MediaUtils.getExtension(filename);

        // then
        assertThat(extension).isEqualTo("jpg");
    }

    @Test
    @DisplayName("멀티파트 파일에서 확장자를 추출한다")
    void getExtension_fromMultipartFile_returnsCorrectExtension() {
        // given
        MockMultipartFile file = new MockMultipartFile(
                "file", "original.png", MediaType.IMAGE_PNG_VALUE, "test".getBytes());

        // when
        String extension = MediaUtils.getExtension(file);

        // then
        assertThat(extension).isEqualTo("png");
    }

    @Test
    @DisplayName("존재하지 않는 미디어 파일 삭제 시 실패한다")
    void deleteMedia_whenFileNotExists_thenReturnsFalse() {
        // given
        String nonExistentMediaUrl = "/media/non-existent-file.jpg";

        // when & then
        assertThatThrownBy(() -> MediaUtils.deleteMedia(nonExistentMediaUrl))
                .isInstanceOf(MediaException.class);
    }

    @Test
    @DisplayName("미디어 파일 삭제 성공 테스트")
    void deleteMedia_Success() {
        // given
        String testFileName = "test-delete-file.txt";
        String mediaUrl = "/media/" + testFileName;
        String fullPath = System.getProperty("user.dir") + mediaUrl;
        
        // 테스트 파일 생성
        try {
            File testFile = new File(fullPath);
            testFile.getParentFile().mkdirs();
            testFile.createNewFile();
        } catch (Exception e) {
            throw new RuntimeException("테스트 파일 생성 실패", e);
        }

        // when
        boolean result = MediaUtils.deleteMedia(mediaUrl);

        // then
        assertThat(result).isTrue();
        assertThat(new File(fullPath).exists()).isFalse();
    }
} 