package com.samsamhajo.deepground.qna.question.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class QuestionUpdateDto {

    private Long questionId;

    @Size(max = 100, message = "제목은 100자 이내로 입력해야 합니다.")
    @NotBlank(message = "제목은 필수입니다.")
    private String title;
    @NotBlank(message = "내용은 필수입니다.")
    private String content;
    private List<Long> techStacks;
    private List<MultipartFile> images;

}
