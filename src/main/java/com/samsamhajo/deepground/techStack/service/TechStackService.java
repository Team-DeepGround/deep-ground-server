package com.samsamhajo.deepground.techStack.service;

import com.samsamhajo.deepground.techStack.dto.TechStackResponseDto;
import com.samsamhajo.deepground.techStack.entity.TechStack;
import com.samsamhajo.deepground.techStack.repository.TechStackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TechStackService {
    private final TechStackRepository techStackRepository;

    public List<TechStackResponseDto> findAllTechStacks() {
        return techStackRepository.findAll().stream()
                .map(ts -> TechStackResponseDto.builder()
                        .id(ts.getId())
                        .name(ts.getName())
                        .category(ts.getCategory())
                        .build())
                .collect(Collectors.toList());
    }
} 