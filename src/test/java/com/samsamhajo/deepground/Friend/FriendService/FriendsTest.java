package com.samsamhajo.deepground.Friend.FriendService;

import com.samsamhajo.deepground.friend.repository.FriendRepository;
import com.samsamhajo.deepground.friend.service.FriendService;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@Transactional
public class FriendsTest {

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
        receiver2 = Member.createLocalMember("pa@gmail.com", "pw", "파");

        memberRepository.save(requester);
        memberRepository.save(requester2);
        memberRepository.save(requester3);
        memberRepository.save(receiver);
        memberRepository.save(receiver2);

    }
//TODO: 친구 목록 API와 머지 후 친구목록에 있는 친구 수로 COUNT해서 테스트
/*    @Test
    public void 친구_삭제_성공() throws Exception {
        //given
        Long friendId = friendService.sendFriendRequest(requester.getId(), receiver.getEmail());
        friendService.acceptFriendRequest(friendId, receiver.getId());
        //when
        friendService.deleteFriendById(friendId);
        //then

    }*/
}