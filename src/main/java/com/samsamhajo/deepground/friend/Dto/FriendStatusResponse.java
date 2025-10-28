package com.samsamhajo.deepground.friend.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class FriendStatusResponse {

    private final ProfileFriendStatus status;

    public FriendStatusResponse(ProfileFriendStatus status) {
        this.status = status;
    }
}
