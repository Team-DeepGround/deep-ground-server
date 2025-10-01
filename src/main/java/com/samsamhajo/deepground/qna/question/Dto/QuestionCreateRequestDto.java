package com.samsamhajo.deepground.qna.question.Dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionCreateRequestDto {

    @NotBlank
    @Size(max = 100, message = "제목은 100자 이내로 작성해주세요.")
    private String title;
    @NotBlank
    @Size(min = 1, message = "내용은 1글자 이상 작성해주세요.")
    private String content;
    private List<String> techStacks;
    private List<MultipartFile> images = new ArrayList<>();
}
