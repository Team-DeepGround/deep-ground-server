package com.samsamhajo.deepground.friend.controller;

import com.samsamhajo.deepground.friend.Dto.FriendDto;
import com.samsamhajo.deepground.friend.Dto.FriendRequestDto;
import com.samsamhajo.deepground.friend.Dto.ResponseDto;
import com.samsamhajo.deepground.friend.service.FriendService;
import com.samsamhajo.deepground.member.entity.Member;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController("/api/v1/friends")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    @PostMapping("/request")
    public ResponseEntity<ResponseDto> requestFriend(@RequestBody @Valid FriendRequestDto dto) {
        Long friendId = friendService.sendFriendRequest(dto.getRequesterId(), dto.getReceiverEmail());
        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "친구 요청을 보냈습니다!", new SendFriendResponse(friendId)));
    }

    @Data
    @AllArgsConstructor
    static class SendFriendResponse {
        private Long id;
    }

    @GetMapping("/sent")
    public ResponseEntity<List<FriendDto>> getSentFriendRequests(@RequestParam Long requesterId) {
        return ResponseEntity.ok(friendService.findSentFriendRequest(requesterId));
    }

}




