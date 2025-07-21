package com.samsamhajo.deepground.communityPlace.controller;

import com.samsamhajo.deepground.communityPlace.service.CommunityPlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/communityplace")
@RequiredArgsConstructor
public class CommunityPlaceController {

    private final CommunityPlaceService communityPlaceService;
}
