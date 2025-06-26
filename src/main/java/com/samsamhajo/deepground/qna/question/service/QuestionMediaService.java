package com.samsamhajo.deepground.qna.question.service;

import com.samsamhajo.deepground.feed.feed.entity.FeedMedia;
import com.samsamhajo.deepground.feed.feed.model.FeedMediaResponse;
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


@Service
@RequiredArgsConstructor
@Transactional
public class QuestionMediaService {

    private final QuestionMediaRepository questionMediaRepository;

    public List<String> createQuestionMedia(Question question, List<MultipartFile> images) {
        if (images == null || images.isEmpty()) {
            return Collections.emptyList();
        }

        List<QuestionMedia> medias = images.stream()
                .map(image -> QuestionMedia.of(
                        MediaUtils.generateMediaUrl(image),
                        MediaUtils.getExtension(image),
                        question))
                .toList();

        questionMediaRepository.saveAll(medias);

        // 저장한 QuestionMedia에서 URL만 추출해서 반환
        return medias.stream()
                .map(QuestionMedia::getMediaUrl)
                .collect(Collectors.toList());
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

