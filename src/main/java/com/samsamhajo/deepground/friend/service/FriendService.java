package com.samsamhajo.deepground.friend.service;

import com.samsamhajo.deepground.friend.Exception.FriendException;
import com.samsamhajo.deepground.friend.entity.Friend;
import com.samsamhajo.deepground.friend.Exception.FriendErrorCode;
import com.samsamhajo.deepground.friend.repository.FriendRepository;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long sendFriendRequest (Long requesterId, String receiverEmail) {

        Member requester = memberRepository.findById(requesterId)
                .orElseThrow(() -> new FriendException(FriendErrorCode.MEMBER_NOT_FOUND));


        Member receiver = memberRepository.findByEmail(receiverEmail)
                .orElseThrow(() -> new FriendException(FriendErrorCode.MEMBER_NOT_FOUND));

        if(receiverEmail == null || receiverEmail.trim().isEmpty()){
            throw new FriendException(FriendErrorCode.BLANK_EMAIL);
        }
        if(requester.getEmail().equals(receiver.getEmail())) {
            throw new FriendException(FriendErrorCode.SELF_REQUEST);
        }
        if(friendRepository.existsByRequestMemberAndReceiveMember(requester,receiver)) {
            throw new FriendException(FriendErrorCode.ALREADY_FRIEND);
        }

        Friend friend = Friend.request(requester, receiver);
        friendRepository.save(friend);

        return friend.getId();
    }



}