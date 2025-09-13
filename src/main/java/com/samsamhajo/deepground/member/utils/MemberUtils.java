package com.samsamhajo.deepground.member.utils;

import com.samsamhajo.deepground.member.entity.Member;

public class MemberUtils {
    public static Long extractProfileId(Member member) {
        if (member == null || member.getMemberProfile() == null) {
            return null;
        }
        return member.getMemberProfile().getProfileId();
    }
}
