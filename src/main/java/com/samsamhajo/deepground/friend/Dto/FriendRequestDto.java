package com.samsamhajo.deepground.friend.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FriendRequestDto {

        @NotBlank(message = "상대방 닉네임을 입력해주세요")
        private String nickname;

}

