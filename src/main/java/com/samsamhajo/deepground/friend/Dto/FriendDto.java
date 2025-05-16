package com.samsamhajo.deepground.friend.Dto;

import com.samsamhajo.deepground.friend.entity.Friend;
import com.samsamhajo.deepground.friend.entity.FriendStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FriendDto {

    private Long friendId;
    private String otherMemberName;
    private FriendStatus status;

    public static FriendDto fromSent(Friend friend) {
        return new FriendDto(
                friend.getId(),
                friend.getReceiveMember().getNickname(),
                friend.getStatus()
        );
    }

}