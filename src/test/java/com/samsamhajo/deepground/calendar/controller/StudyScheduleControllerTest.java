package com.samsamhajo.deepground.calendar.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.samsamhajo.deepground.calendar.dto.StudyScheduleRequestDto;
import com.samsamhajo.deepground.calendar.dto.StudyScheduleResponseDto;
import com.samsamhajo.deepground.calendar.service.StudyScheduleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StudyScheduleController.class)
public class StudyScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudyScheduleService studyScheduleService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void createStudySchedule_Success() throws Exception {
        // given
        StudyScheduleRequestDto requestDto = StudyScheduleRequestDto.builder()
                .title("Spring Boot Study")
                .startTime(LocalDateTime.of(2025, 5, 20, 14, 0))
                .endTime(LocalDateTime.of(2025, 5, 20, 16, 0))
                .description("Spring Boot 학습")
                .location("온라인")
                .build();

        StudyScheduleResponseDto responseDto = StudyScheduleResponseDto.builder()
                .id(1L)
                .title(requestDto.getTitle())
                .startTime(requestDto.getStartTime())
                .endTime(requestDto.getEndTime())
                .description(requestDto.getDescription())
                .location(requestDto.getLocation())
                .build();

        when(studyScheduleService.createStudySchedule(anyLong(), any(StudyScheduleRequestDto.class)))
                .thenReturn(responseDto);

        // when & then
        mockMvc.perform(post("/study-group/1/schedules", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Spring Boot Study"))
                .andExpect(jsonPath("$.startTime").value("2025-05-20T14:00:00"))
                .andExpect(jsonPath("$.endTime").value("2025-05-20T16:00:00"))
                .andExpect(jsonPath("$.description").value("Spring Boot 학습"))
                .andExpect(jsonPath("$.location").value("온라인"));

    }
}
