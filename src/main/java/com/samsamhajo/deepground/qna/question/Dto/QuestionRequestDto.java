package com.samsamhajo.deepground.qna.question.Dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@AllArgsConstructor
public class QuestionRequestDto {

    @Size(max = 100, message = "제목은 100자 이내로 작성해주세요.")
    private String title;
    private String content;
    private List<Long> techStacksId;
    private List<MultipartFile> mediaFiles;

}
