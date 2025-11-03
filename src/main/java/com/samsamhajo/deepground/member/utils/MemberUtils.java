package com.samsamhajo.deepground.member.utils;

import com.samsamhajo.deepground.member.entity.Member;

import java.util.UUID;

public class MemberUtils {
    public static UUID extractProfilePublicId(Member member) {
        if (member == null || member.getMemberProfile() == null) {
            return null;
        }
        return member.getMemberProfile().getProfilePublicId();
    }
}
