package com.samsamhajo.deepground.qna.answer.service;

import com.nimbusds.oauth2.sdk.util.CollectionUtils;
import com.samsamhajo.deepground.global.upload.S3Uploader;
import com.samsamhajo.deepground.global.upload.exception.UploadErrorCode;
import com.samsamhajo.deepground.global.upload.exception.UploadException;
import com.samsamhajo.deepground.media.MediaUtils;
import com.samsamhajo.deepground.qna.answer.entity.Answer;
import com.samsamhajo.deepground.qna.answer.entity.AnswerMedia;
import com.samsamhajo.deepground.qna.answer.repository.AnswerMediaRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.samsamhajo.deepground.media.MediaUtils.getExtension;

@Service
@RequiredArgsConstructor
public class AnswerMediaService {

    private final AnswerMediaRepository answerMediaRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    public void createAnswerMedia(Answer answer, List<MultipartFile> mediaFiles) {
        if (CollectionUtils.isEmpty(mediaFiles)) return;

        List<AnswerMedia> mediaEntities = mediaFiles.stream()
                .map(file -> {
                    String url = s3Uploader.upload(file, "answer-media");
                    String extension = getExtension(file.getOriginalFilename());
                    return AnswerMedia.of(url, extension, answer);
                })
                .toList();
        answerMediaRepository.saveAll(mediaEntities);
    }

    @Transactional
    public void deleteAnswerMedia(Long answerId) {
        List<AnswerMedia> answerMedia = answerMediaRepository.findAllByAnswerId(answerId);
        answerMedia.forEach(content -> MediaUtils.deleteMedia(content.getAnswerCommentUrl()));
        answerMediaRepository.deleteAllByAnswerId(answerId);
    }

}