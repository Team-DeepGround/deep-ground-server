package com.samsamhajo.deepground.qna.answer.service;

import com.nimbusds.oauth2.sdk.util.CollectionUtils;
import com.samsamhajo.deepground.global.upload.S3Uploader;
import com.samsamhajo.deepground.media.MediaUtils;
import com.samsamhajo.deepground.qna.answer.entity.Answer;
import com.samsamhajo.deepground.qna.answer.entity.AnswerMedia;
import com.samsamhajo.deepground.qna.answer.repository.AnswerMediaRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.samsamhajo.deepground.media.MediaUtils.getExtension;

@Service
@RequiredArgsConstructor
@Transactional
public class AnswerMediaService {

    private final AnswerMediaRepository answerMediaRepository;
    private final S3Uploader s3Uploader;


    public List<String> createAnswerMedia(Answer answer, List<MultipartFile> images) {
        if (images == null || images.isEmpty()) {
            return Collections.emptyList();
        }

        List<AnswerMedia> mediaEntities = images.stream()
                .map(file -> {
                    String url = s3Uploader.upload(file, "answer-media");
                    String extension = getExtension(file.getOriginalFilename());
                    return AnswerMedia.of(url, extension, answer);
                })
                .collect(Collectors.toList());

        answerMediaRepository.saveAll(mediaEntities);
        return mediaEntities.stream().map(AnswerMedia::getMediaUrl).toList();
    }

    public void deleteAnswerMedia(Long answerId) {
        List<AnswerMedia> answerMediaList = answerMediaRepository.findAllByAnswerId(answerId);

        if (answerMediaList.isEmpty()) {
            // 미디어 없으면 아무 것도 하지 않고 그냥 return
            return;
        }

        // S3 등 외부 저장소 미디어 삭제
        answerMediaList.forEach(media -> {
            try {
                MediaUtils.deleteMedia(media.getMediaUrl());
            } catch (Exception e) {

            }
        });

        // DB에서 연관 미디어 삭제
        answerMediaRepository.deleteAllByAnswerId(answerId);
    }

    public Optional<AnswerMedia> findByMediaUrl(String mediaUrl) {
        return answerMediaRepository.findByMediaUrl(mediaUrl);
    }

}