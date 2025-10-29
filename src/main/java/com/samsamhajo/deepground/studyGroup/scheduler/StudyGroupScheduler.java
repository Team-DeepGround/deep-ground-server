package com.samsamhajo.deepground.studyGroup.scheduler;

import com.samsamhajo.deepground.studyGroup.service.StudyGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;


@Component
@RequiredArgsConstructor
public class StudyGroupScheduler {

    private final StudyGroupService studyGroupService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void changeStudyStatus() {

        LocalDate today = LocalDate.now();

        studyGroupService.updateStudyGroupStatus(today);
    }
}
