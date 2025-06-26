package com.samsamhajo.deepground.qna.question.service;


import com.nimbusds.oauth2.sdk.util.CollectionUtils;
import com.samsamhajo.deepground.global.upload.S3Uploader;
import com.samsamhajo.deepground.media.MediaUtils;
import com.samsamhajo.deepground.qna.question.Dto.QuestionMediaResponse;
import com.samsamhajo.deepground.qna.question.entity.Question;
import com.samsamhajo.deepground.qna.question.entity.QuestionMedia;
import com.samsamhajo.deepground.qna.question.repository.QuestionMediaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.samsamhajo.deepground.media.MediaUtils.getExtension;


@Service
@RequiredArgsConstructor
@Transactional
public class QuestionMediaService {

    private final QuestionMediaRepository questionMediaRepository;
    private final S3Uploader s3Uploader;


    public List<String> createQuestionMedia(Question question, List<MultipartFile> images) {
        if (CollectionUtils.isEmpty(images)){
            throw new IllegalArgumentException("image is blank");
        }

        List<QuestionMedia> mediaEntities = images.stream()
                .map(image -> {
                    String url = s3Uploader.upload(image, "question-media");
                    String extension = getExtension(image.getOriginalFilename());
                    return QuestionMedia.of(url, extension, question);
                })
                .collect(Collectors.toList());

        questionMediaRepository.saveAll(mediaEntities);
        return mediaEntities.stream().map(QuestionMedia::getMediaUrl).toList();
    }


    public void deleteQuestionMedia(Long questionId) {
        List<QuestionMedia> questionMedia = questionMediaRepository.findAllByQuestionId(questionId);
        questionMedia.forEach(content -> MediaUtils.deleteMedia(content.getMediaUrl()));
        questionMediaRepository.deleteAllByQuestionId(questionId);
    }

    public QuestionMediaResponse questionFetchMedia(String mediaUrl) {
        QuestionMedia questionMedia = questionMediaRepository.findByMediaUrl(mediaUrl)
                .orElseThrow(() -> new NotFoundException("이미지를 찾을 수 없습니다."));

        InputStreamResource media = MediaUtils.getMedia(questionMedia.getMediaUrl());

        return QuestionMediaResponse.of(media, questionMedia.getExtension());
    }

    public Optional<QuestionMedia> findByMediaUrl(String mediaUrl) {
        return questionMediaRepository.findByMediaUrl(mediaUrl);
    }

}

