package com.samsamhajo.deepground.qna.question.service;

import com.nimbusds.oauth2.sdk.util.CollectionUtils;
import com.samsamhajo.deepground.global.upload.S3Uploader;
import com.samsamhajo.deepground.media.MediaUtils;
import com.samsamhajo.deepground.qna.question.entity.Question;
import com.samsamhajo.deepground.qna.question.entity.QuestionMedia;
import com.samsamhajo.deepground.qna.question.repository.QuestionMediaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import static com.samsamhajo.deepground.media.MediaUtils.getExtension;


@Service
@RequiredArgsConstructor
@Transactional
public class QuestionMediaService {

    private final QuestionMediaRepository questionMediaRepository;
    private final S3Uploader s3Uploader;

    public void createQuestionMedia(Question question, List<MultipartFile> images) {
        if (CollectionUtils.isEmpty(images)) return;

        List<QuestionMedia> mediaEntities = images.stream()
                .map(image -> {
                    String url = s3Uploader.upload(image, "question-media");
                    String extension = getExtension(image.getOriginalFilename());
                    return QuestionMedia.of(url, extension, question);
                })
                .collect(Collectors.toList());

        questionMediaRepository.saveAll(mediaEntities);
    }


    public void deleteQuestionMedia(Long questionId) {
        List<QuestionMedia> questionMedia = questionMediaRepository.findAllByQuestionId(questionId);
        questionMedia.forEach(content -> MediaUtils.deleteMedia(content.getQuestionContentUrl()));
        questionMediaRepository.deleteAllByQuestionId(questionId);
    }

}

