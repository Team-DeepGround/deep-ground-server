package com.samsamhajo.deepground.friend.controller;


import com.samsamhajo.deepground.auth.security.CustomUserDetails;
import com.samsamhajo.deepground.friend.Dto.FriendDto;
import com.samsamhajo.deepground.friend.Dto.FriendRequestDto;
import com.samsamhajo.deepground.friend.Exception.FriendSuccessCode;
import com.samsamhajo.deepground.friend.service.FriendService;
import com.samsamhajo.deepground.global.success.SuccessResponse;
import com.samsamhajo.deepground.global.utils.GlobalLogger;
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
    public ResponseEntity<SuccessResponse> requestFriend(@RequestBody @Valid FriendRequestDto dto,
                                                         @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long memberId = userDetails.getMember().getId();
        Long friendId = friendService.sendFriendRequest(memberId, dto.getReceiverEmail());
        GlobalLogger.info("friend request log={}","요청 멤버 id:"+ memberId+"친구 고유 id:"+friendId);

        return ResponseEntity
                        .ok(SuccessResponse.of(FriendSuccessCode.FRIEND_SUCCESS_REQUEST,friendId));
    }


    @GetMapping("/sent")
    public ResponseEntity<SuccessResponse<List<FriendDto>>> getSentFriendRequests( @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long memberId = userDetails.getMember().getId();
        List<FriendDto> sentList = friendService.findSentFriendRequest(memberId);
        return ResponseEntity.ok(
                SuccessResponse.of(FriendSuccessCode.FRIEND_SENT_LIST_FOUND, sentList));
    }


    @PatchMapping("/sent/{friendId}/cancel")
    public ResponseEntity<SuccessResponse> cancelFriendRequest(@PathVariable Long friendId,
                                                               @AuthenticationPrincipal CustomUserDetails userDetails){
        Long memberId = userDetails.getMember().getId();
        friendService.cancelFriendRequest(friendId, memberId);
        return ResponseEntity
                .ok(SuccessResponse.of(FriendSuccessCode.FRIEND_SUCCESS_CANCEL,friendId));

    }

    @GetMapping("/receive")
    public ResponseEntity<SuccessResponse<List<FriendDto>>> getReceiveFriendRequests( @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long memberId = userDetails.getMember().getId();
        List<FriendDto> receiveList = friendService.findFriendReceive(memberId);
        return ResponseEntity
                .ok(SuccessResponse.of(FriendSuccessCode.FRIEND_RECEIVE_LIST_FOUND, receiveList));
    }

    @PatchMapping("/receive/{friendId}/accept")
    public ResponseEntity<SuccessResponse> acceptFriendRequest(@PathVariable Long friendId,
                                                               @AuthenticationPrincipal CustomUserDetails userDetails){
        Long memberId = userDetails.getMember().getId();
        Long result = friendService.acceptFriendRequest(friendId, memberId);
        return ResponseEntity
                .ok(SuccessResponse.of(FriendSuccessCode.FRIEND_SUCCESS_ACCEPT,result));
    }

    @PatchMapping("/receive/{friendId}/refusal")
    public ResponseEntity<SuccessResponse> refusalFriendRequest(@PathVariable Long friendId,
                                                                @AuthenticationPrincipal CustomUserDetails userDetails){
        Long memberId = userDetails.getMember().getId();
        friendService.refusalFriendRequest(friendId,memberId);
        return ResponseEntity
                .ok(SuccessResponse.of(FriendSuccessCode.FRIEND_SUCCESS_REFUSAL,friendId));

    }

    @PostMapping("/from-profile/{receiverId}")
    public ResponseEntity<SuccessResponse> requestProfileFriend(@PathVariable Long receiverId,
                                                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long memberId = userDetails.getMember().getId();
        Long friendId = friendService.sendProfileFriendRequest(memberId, receiverId);
        GlobalLogger.info("friend request from profile={}","요청 멤버 id:"+ memberId+"고유 친구 id:"+friendId);


        return ResponseEntity
                .ok(SuccessResponse.of(FriendSuccessCode.FRIEND_SUCCESS_REQUEST,friendId));
    }

    @DeleteMapping("/{friendId}")
    public ResponseEntity<SuccessResponse> deleteFriend(@PathVariable Long friendId,
                                                        @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long memberId = userDetails.getMember().getId();
        friendService.deleteFriendByMemberId(friendId, memberId);
        return ResponseEntity
                .ok(SuccessResponse.of(FriendSuccessCode.FRIEND_SUCCESS_DELETE, friendId));
    }

    @GetMapping
    public  ResponseEntity<SuccessResponse<List<FriendDto>>> getFriendList (@AuthenticationPrincipal CustomUserDetails userDetails){

        Long memberId = userDetails.getMember().getId();
        List<FriendDto> friends = friendService.getFriendByMemberId(memberId);
        return ResponseEntity
                .ok(SuccessResponse.of(FriendSuccessCode.FRIEND_SUCCESS_GET_LIST,friends));
    }
}
