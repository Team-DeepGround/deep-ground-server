package com.samsamhajo.deepground.friend.controller;

import com.samsamhajo.deepground.friend.service.FriendService;
import com.samsamhajo.deepground.member.entity.Member;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    @PostMapping("/api/v1/friends/request")
    public SendFriendResponse requestFriend(@RequestBody @Valid FriendRequestDto dto) {
        Long friendId = friendService.sendFriendRequest(dto.getRequesterId(),dto.getReceiverEmail());
        return new SendFriendResponse(friendId);
    }


    @Data
    static class FriendRequestDto {
    private Long requesterId;
    private String receiverEmail;
    }

    @Data
    @AllArgsConstructor
    static class SendFriendResponse  {
        private Long id;
    }



}
