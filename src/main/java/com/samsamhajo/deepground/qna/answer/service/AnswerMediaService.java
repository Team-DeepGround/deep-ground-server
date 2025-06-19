package com.samsamhajo.deepground.qna.answer.service;

import com.samsamhajo.deepground.media.MediaUtils;
import com.samsamhajo.deepground.qna.answer.entity.Answer;
import com.samsamhajo.deepground.qna.answer.entity.AnswerMedia;
import com.samsamhajo.deepground.qna.answer.repository.AnswerMediaRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnswerMediaService {

    private final AnswerMediaRepository answerMediaRepository;

    @Transactional
    public void createAnswerMedia(Answer answer, List<MultipartFile> mediaFiles) {

        answerMediaRepository.saveAll(
                mediaFiles.stream()
                        .map(mediaFile -> AnswerMedia.of(
                                MediaUtils.generateMediaUrl(mediaFile),
                                MediaUtils.getExtension(mediaFile),
                                answer))
                        .toList()
        );
    }

    @Transactional
    public void deleteAnswerMedia(Long answerId) {
        List<AnswerMedia> answerMedia = answerMediaRepository.findAllByAnswerId(answerId);
        answerMedia.forEach(content -> MediaUtils.deleteMedia(content.getAnswerCommentUrl()));
        answerMediaRepository.deleteAllByAnswerId(answerId);
    }

}