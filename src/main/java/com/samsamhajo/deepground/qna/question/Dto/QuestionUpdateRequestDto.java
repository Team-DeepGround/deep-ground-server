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
@NoArgsConstructor
@AllArgsConstructor
public class QuestionUpdateRequestDto {

    private Long questionId;

    @Size(max = 100, message = "제목은 100자 이내로 입력해야 합니다.")
    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    @Size(min = 1, message = "내용은 1글자 이상 입력해야 합니다.")
    @NotBlank(message = "내용은 필수입니다.")
    private String content;

    private List<String> techStacks;
    private List<MultipartFile> images = new ArrayList<>();

}
