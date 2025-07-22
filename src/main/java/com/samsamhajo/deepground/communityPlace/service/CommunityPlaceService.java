package com.samsamhajo.deepground.communityPlace.service;

import com.samsamhajo.deepground.communityPlace.repository.CommunityPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommunityPlaceService {

    private final CommunityPlaceRepository communityPlaceRepository;


}
