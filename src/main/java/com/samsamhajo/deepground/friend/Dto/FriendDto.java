package com.samsamhajo.deepground.friend.Dto;

import com.samsamhajo.deepground.friend.entity.Friend;
import com.samsamhajo.deepground.friend.entity.FriendStatus;
import lombok.Getter;

import java.util.UUID;

@Getter
public class FriendDto {

    private Long friendId;
    private String otherMemberNickname;
    private FriendStatus status;
    private UUID profilePublicId;

    private FriendDto(Long friendId, String otherMemberNickname, UUID profilePublicId, FriendStatus status) {
        this.friendId = friendId;
        this.otherMemberNickname = otherMemberNickname;
        this.profilePublicId = profilePublicId;
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
