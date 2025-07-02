package com.samsamhajo.deepground.qna.answer.dto;


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
public class AnswerCreateRequestDto {

    @NotBlank(message = "댓글 내용은 비어 있을 수 없습니다.")
    @Size(min = 1, message = "2글자 이상은 입력하셔야합니다.")
    private String answerContent;
    private List<MultipartFile> images = new ArrayList<>();
    private Long questionId;

}