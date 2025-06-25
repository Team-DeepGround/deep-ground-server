package com.samsamhajo.deepground.qna.question.service;

import com.samsamhajo.deepground.media.MediaUtils;
import com.samsamhajo.deepground.qna.question.entity.Question;
import com.samsamhajo.deepground.qna.question.entity.QuestionMedia;
import com.samsamhajo.deepground.qna.question.repository.QuestionMediaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;



@Service
@RequiredArgsConstructor
@Transactional
public class QuestionMediaService {

    private final QuestionMediaRepository questionMediaRepository;

    public void createQuestionMedia(Question question, List<MultipartFile> images) {

        questionMediaRepository.saveAll(
                images.stream()
                        .map(image -> QuestionMedia.of(
                                MediaUtils.generateMediaUrl(image),
                                MediaUtils.getExtension(image),
                                question))
                        .toList()
        );
    }


    public void deleteQuestionMedia(Long questionId) {
        List<QuestionMedia> questionMedia = questionMediaRepository.findAllByQuestionId(questionId);
        questionMedia.forEach(content -> MediaUtils.deleteMedia(content.getMediaUrl()));
        questionMediaRepository.deleteAllByQuestionId(questionId);
    }

}

