package com.samsamhajo.deepground.studyGroup.controller;

import com.samsamhajo.deepground.global.success.SuccessResponse;
import com.samsamhajo.deepground.global.utils.GlobalLogger;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupsByLocationResponse;
import com.samsamhajo.deepground.studyGroup.service.StudyGroupLocationService;
import com.samsamhajo.deepground.studyGroup.success.StudyGroupSuccessCode;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/study-group")
@RequiredArgsConstructor
public class StudyGroupLocationController {

    private final StudyGroupLocationService studyGroupLocationService;

    @GetMapping("/list")
    public ResponseEntity<SuccessResponse<?>> getStudyGroupLocationList(
            @RequestParam @NotNull String city,
            @RequestParam @NotNull String gu,
            @RequestParam @NotNull String dong
    ) {
        GlobalLogger.info("스터디 그룹 목록 조회", city, gu, dong);

        StudyGroupsByLocationResponse response = studyGroupLocationService.getStudyGroupsByLocation(city, gu, dong);

        return ResponseEntity
                .status(StudyGroupSuccessCode.SEARCH_SUCCESS.getStatus())
                .body(SuccessResponse.of(StudyGroupSuccessCode.SEARCH_SUCCESS, response));
    }
}
