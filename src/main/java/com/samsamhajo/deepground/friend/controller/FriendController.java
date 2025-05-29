package com.samsamhajo.deepground.friend.controller;


import com.samsamhajo.deepground.friend.Dto.FriendDto;
import com.samsamhajo.deepground.friend.Dto.FriendRequestDto;
import com.samsamhajo.deepground.friend.Exception.FriendSuccessCode;
import com.samsamhajo.deepground.friend.service.FriendService;
import com.samsamhajo.deepground.global.success.SuccessResponse;
import com.samsamhajo.deepground.member.entity.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(("/friends"))
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    @PostMapping("/request")
    public ResponseEntity<SuccessResponse> requestFriend(@RequestBody @Valid FriendRequestDto dto) {
        Long friendId = friendService.sendFriendRequest(dto.getRequesterId(), dto.getReceiverEmail());
        return ResponseEntity
                        .ok(SuccessResponse.of(FriendSuccessCode.FRIEND_SUCCESS_REQUEST,friendId));
    }


    @GetMapping("/sent")
    public ResponseEntity<SuccessResponse<List<FriendDto>>> getSentFriendRequests(@RequestParam Long requesterId) {
        List<FriendDto> sentList = friendService.findSentFriendRequest(requesterId);
        return ResponseEntity.ok(
                SuccessResponse.of(FriendSuccessCode.FRIEND_SENT_LIST_FOUND, sentList));
    }


    @PatchMapping("/sent/{friendId}/cancel")
    public ResponseEntity<SuccessResponse> cancelFriendRequest(@PathVariable Long friendId,
                                                           @RequestParam Long requesterId){
        friendService.cancelFriendRequest(friendId,requesterId);
        return ResponseEntity
                .ok(SuccessResponse.of(FriendSuccessCode.FRIEND_SUCCESS_CANCEL,friendId));

    }

    @GetMapping("/receive")
    public ResponseEntity<SuccessResponse<List<FriendDto>>> getReceiveFriendRequests(@RequestParam Long receiverId) {
        List<FriendDto> receiveList = friendService.findFriendReceive(receiverId);
        return ResponseEntity
                .ok(SuccessResponse.of(FriendSuccessCode.FRIEND_RECEIVE_LIST_FOUND, receiveList));
    }

    @PatchMapping("/receive/{friendId}/accept")
    public ResponseEntity<SuccessResponse> acceptFriendRequest(@PathVariable Long friendId,
                                                               @RequestParam Long receiverId){
        Long result = friendService.acceptFriendRequest(friendId, receiverId);
        return ResponseEntity
                .ok(SuccessResponse.of(FriendSuccessCode.FRIEND_SUCCESS_ACCEPT,result));
    }

    @PatchMapping("/receive/{friendId}/refusal")
    public ResponseEntity<SuccessResponse> refusalFriendRequest(@PathVariable Long friendId,
                                                                @RequestParam Long receiverId){
        friendService.refusalFriendRequest(friendId,receiverId);
        return ResponseEntity
                .ok(SuccessResponse.of(FriendSuccessCode.FRIEND_SUCCESS_REFUSAL,friendId));

    }

    @PostMapping("/from-profile/{receiverId}")
    public ResponseEntity<SuccessResponse> requestProfileFriend(@PathVariable Long receiverId,
                                                                @AuthenticationPrincipal Member requester) {
        Long friendId = friendService.sendProfileFriendRequest(requester.getId(), receiverId);
        return ResponseEntity
                .ok(SuccessResponse.of(FriendSuccessCode.FRIEND_SUCCESS_REQUEST,friendId));
    }

    @DeleteMapping("/{friendId}")
    public ResponseEntity<SuccessResponse> deleteFriend(@PathVariable Long friendId){

        friendService.deleteFriendById(friendId);
        return ResponseEntity
                .ok(SuccessResponse.of(FriendSuccessCode.FRIEND_SUCCESS_DELETE, friendId));
    }
  


}
