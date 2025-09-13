package com.samsamhajo.deepground.qna.question.Dto;

import lombok.Getter;
import org.springframework.core.io.InputStreamResource;

@Getter
public class QuestionMediaResponse {

    private final InputStreamResource image;
    private final String extension;

    private QuestionMediaResponse(InputStreamResource image, String extension) {
        this.image = image;
        this.extension = extension;
    }

    public static QuestionMediaResponse of(InputStreamResource image, String extension) {
        return new QuestionMediaResponse(image, extension);
    }
}