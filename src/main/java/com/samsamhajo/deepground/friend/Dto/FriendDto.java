package com.samsamhajo.deepground.friend.Dto;

import com.samsamhajo.deepground.friend.entity.Friend;
import com.samsamhajo.deepground.friend.entity.FriendStatus;
import lombok.Getter;

import java.util.UUID;

@Getter
public class FriendDto {

    private Long friendId;
    private String otherMemberName;
    private FriendStatus status;
    private UUID getProfilePublicId;

    private FriendDto(Long friendId, String otherMemberName, UUID getProfilePublicId, FriendStatus status) {
        this.friendId = friendId;
        this.otherMemberName = otherMemberName;
        this.getProfilePublicId = getProfilePublicId;
        this.status = status;
    }

    public static FriendDto fromSent(Friend friend) {
        return new FriendDto(
                friend.getId(),
                friend.getReceiveMember().getNickname(),
                friend.getReceiveMember().getMemberProfile() != null
                        ? friend.getReceiveMember().getMemberProfile().getProfilePublicId()
                        : null,
                friend.getStatus()
        );
    }

    public static FriendDto fromReceived(Friend friend) {
        return new FriendDto(
                friend.getId(),
                friend.getRequestMember().getNickname(),
                friend.getReceiveMember().getMemberProfile() != null
                        ? friend.getReceiveMember().getMemberProfile().getProfilePublicId()
                        : null,
                friend.getStatus()
        );
    }
}
