package com.samsamhajo.deepground.qna.question.Dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
public class QuestionRequestDto {

    @Size(max = 100, message = "제목은 200자 이내로 작성해주세요.")
    private String title;
    private String content;
    private List<Long> techStacksId;
    private List<MultipartFile> mediaFiles;

}
