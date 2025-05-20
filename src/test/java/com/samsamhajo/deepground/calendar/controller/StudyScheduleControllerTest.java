package com.samsamhajo.deepground.calendar.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.samsamhajo.deepground.calendar.dto.StudyScheduleRequestDto;
import com.samsamhajo.deepground.calendar.dto.StudyScheduleResponseDto;
import com.samsamhajo.deepground.calendar.exception.ScheduleSuccessCode;
import com.samsamhajo.deepground.calendar.service.StudyScheduleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
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
    @WithMockUser(username = "testUser")
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
        mockMvc.perform(post("/study-group/{studyGroupId}/schedules", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.result.id").value(1L))
                .andExpect(jsonPath("$.result.title").value("Spring Boot Study"))
                .andExpect(jsonPath("$.result.startTime").value("2025-05-20T14:00:00"))
                .andExpect(jsonPath("$.result.endTime").value("2025-05-20T16:00:00"))
                .andExpect(jsonPath("$.result.description").value("Spring Boot 학습"))
                .andExpect(jsonPath("$.result.location").value("온라인"));

    }

    @Test
    @WithMockUser(username = "testUser")
    void getStudySchedulesByGroup_Success() throws Exception {
        // given
        StudyScheduleResponseDto schedule1 = StudyScheduleResponseDto.builder()
                .id(1L)
                .title("Spring Study 1")
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(2))
                .description("설명")
                .location("Online")
                .build();

        StudyScheduleResponseDto schedule2 = StudyScheduleResponseDto.builder()
                .id(2L)
                .title("Spring Study 2")
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(2))
                .description("설명")
                .location("Offline")
                .build();

        when(studyScheduleService.findSchedulesByStudyGroupId(1L)).thenReturn(List.of(schedule1, schedule2));

        // when & then
        mockMvc.perform(get("/study-group/{studyGroupId}/schedules", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(ScheduleSuccessCode.SCHEDULE_FOUND.getStatus().value()))
                .andExpect(jsonPath("$.result[0].title").value("Spring Study 1"))
                .andExpect(jsonPath("$.result[1].title").value("Spring Study 2"));

    }
}
