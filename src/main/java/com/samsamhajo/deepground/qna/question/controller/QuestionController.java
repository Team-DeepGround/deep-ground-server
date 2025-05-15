package com.samsamhajo.deepground.qna.question.controller;

import com.samsamhajo.deepground.qna.question.Dto.QuestionRequestDto;
import com.samsamhajo.deepground.qna.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> creatQuestion(
            @ModelAttribute QuestionRequestDto questionRequestDto,
            @RequestParam Long memberId) {

        Long questionId = questionService.createQuestion(questionRequestDto, memberId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(questionId);
    }
}
