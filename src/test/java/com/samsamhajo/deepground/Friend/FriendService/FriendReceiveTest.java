package com.samsamhajo.deepground.Friend.FriendService;

import com.samsamhajo.deepground.friend.Dto.FriendDto;
import com.samsamhajo.deepground.friend.Dto.FriendRequestDto;
import com.samsamhajo.deepground.friend.Exception.FriendErrorCode;
import com.samsamhajo.deepground.friend.Exception.FriendException;
import com.samsamhajo.deepground.friend.entity.Friend;
import com.samsamhajo.deepground.friend.entity.FriendStatus;
import com.samsamhajo.deepground.friend.repository.FriendRepository;
import com.samsamhajo.deepground.friend.service.FriendService;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class FriendReceiveTest {

    @Autowired
    private FriendService friendService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FriendRepository friendRepository;


    private Member requester;
    private Member requester2;
    private Member requester3;
    private Member receiver;
    private Member receiver2;



    @BeforeEach
    void setup() {
        requester = Member.createLocalMember("paka@gamil.com", "pw", "파카");
        requester2 = Member.createLocalMember("gar@gmail.com", "pw", "가");
        requester3 = Member.createLocalMember("den@gmail.com", "pw", "든");
        receiver = Member.createLocalMember("garden@gmail.com", "pw", "가든");
        receiver2 = Member.createLocalMember("pa@gmail.com","pw","파카");




        memberRepository.save(requester);
        memberRepository.save(requester3);
        memberRepository.save(requester2);
        memberRepository.save(receiver);
        memberRepository.save(receiver2);

    }

    @Test
    public void 친구_요청_거절() throws Exception {
        //given
        Long friendId = friendService.sendFriendRequest(requester.getId(), receiver.getEmail());

        //when
        Long refusal = friendService.refusalFriendRequest(friendId, receiver.getId());

        //then
        Friend request = friendRepository.findById(friendId).orElseThrow();
        assertEquals(FriendStatus.REFUSAL, request.getStatus());
    }

    @Test
    public void 친구_요청_수락() throws Exception {
        //given
        Long friendId = friendService.sendFriendRequest(requester.getId(), receiver.getEmail());

        //when
        Long accept = friendService.acceptFriendRequest(friendId, receiver.getId());

        //then
        assertEquals(friendId ,accept);
    }

    @Test
    public void 다른_사용자_수락_예외() throws Exception {
        //given
        Long friendId = friendService.sendFriendRequest(requester.getId(), receiver.getEmail());

        //when
        FriendException exception = assertThrows(FriendException.class, () ->

                friendService.refusalFriendRequest(friendId, receiver2.getId()));
        //then
        assertEquals(FriendErrorCode.UNAUTHORIZED_ACCESS, exception.getErrorCode());
        }

    @Test
    public void 이미_친구_상태에서_수락시_예외() throws Exception {

        //given
        Friend friend = Friend.request(requester, receiver);
        friend.accept();
        friendRepository.save(friend);

        //when,then
        FriendException exception = assertThrows(FriendException.class, () ->

                friendService.refusalFriendRequest(friend.getId(), receiver.getId()));

        assertEquals(FriendErrorCode.ALREADY_FRIEND, exception.getErrorCode());
    }


}

    public void 받은_친구_요청_목록() throws Exception {
        //given
        friendService.sendFriendRequest(requester.getId(), receiver.getEmail());
        friendService.sendFriendRequest(requester2.getId(), receiver.getEmail());
        friendService.sendFriendRequest(requester3.getId(), receiver.getEmail());
        //when
        List<FriendDto> receiveList = friendService.findFriendReceive(receiver.getId());
        //then
        assertEquals(3, receiveList.size());


        assertTrue(receiveList.stream().anyMatch(f -> f.getOtherMemberName().equals(requester.getNickname())));
        assertTrue(receiveList.stream().anyMatch(f -> f.getOtherMemberName().equals(requester2.getNickname())));
        assertTrue(receiveList.stream().anyMatch(f -> f.getOtherMemberName().equals(requester3.getNickname())));

    }

}

