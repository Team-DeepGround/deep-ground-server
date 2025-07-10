package com.samsamhajo.deepground.techStack.controller;

import com.samsamhajo.deepground.global.success.SuccessResponse;
import com.samsamhajo.deepground.techStack.dto.TechStackResponseDto;
import com.samsamhajo.deepground.techStack.service.TechStackService;
import com.samsamhajo.deepground.techStack.success.TechStackSuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tech-stack")
public class TechStackController {
    private final TechStackService techStackService;

    @GetMapping
    public ResponseEntity<SuccessResponse<List<TechStackResponseDto>>> getAllTechStacks() {
        List<TechStackResponseDto> response = techStackService.findAllTechStacks();
        return ResponseEntity
                .status(TechStackSuccessCode.READ_SUCCESS.getStatus())
                .body(SuccessResponse.of(TechStackSuccessCode.READ_SUCCESS, response));
    }
} 