package com.samsamhajo.deepground.studyGroup.service;

import com.samsamhajo.deepground.studyGroup.dto.StudyGroupResponse;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupsByLocationResponse;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroup;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupAddressRepository;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyGroupLocationService {

    private final StudyGroupRepository studyGroupRepository;
    private final StudyGroupAddressRepository studyGroupAddressRepository;

    public StudyGroupsByLocationResponse getStudyGroupsByLocation(String city, String gu, String dong) {
        List<Long> studyGroupIds = studyGroupAddressRepository.findStudyGroupIdByAddress(city, gu, dong);
        List<StudyGroup> studyGroups = studyGroupRepository.findByIdIn(studyGroupIds);

        List<StudyGroupResponse> studyGroupResponses = studyGroups.stream()
                .map(StudyGroupResponse::from)
                .toList();

        return StudyGroupsByLocationResponse.of(studyGroupResponses);
    }
}
