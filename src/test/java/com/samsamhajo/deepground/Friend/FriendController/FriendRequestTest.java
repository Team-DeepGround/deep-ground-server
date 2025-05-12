package com.samsamhajo.deepground.Friend.FriendController;

import com.samsamhajo.deepground.friend.Exception.FriendErrorCode;
import com.samsamhajo.deepground.friend.Exception.FriendException;
import com.samsamhajo.deepground.friend.entity.Friend;
import com.samsamhajo.deepground.friend.entity.FriendStatus;
import com.samsamhajo.deepground.friend.repository.FriendRepository;
import com.samsamhajo.deepground.friend.service.FriendService;

import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class FriendRequestTest {
    @Autowired
    private FriendService friendService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FriendRepository friendRepository;



    private Member requester;
    private Member receiver;

    @BeforeEach
    void setup() {
        requester = Member.createLocalMember("paka@gamil.com", "pw", "파카");
        receiver = Member.createLocalMember("garden@gmail.com", "pw", "가든");


        memberRepository.save(requester);
        memberRepository.save(receiver);
    }

    @Test
    public void 친구_요청() throws Exception {
        //given
        Long friendId = friendService.sendFriendRequest(requester.getId(), receiver.getEmail());
        //when
        Friend saved = friendRepository.findById(friendId).get();

        //then
        assertEquals(requester.getId(), saved.getRequestMember().getId());
        assertEquals(receiver.getEmail(), saved.getReceiveMember().getEmail());
        assertEquals(FriendStatus.REQUEST, saved.getStatus());
    }

    @Test
    public void 없는_이메일_예외() throws Exception {
        //given
        friendService.sendFriendRequest(requester.getId(), receiver.getEmail());
        //when,then
        FriendException exception = assertThrows(FriendException.class, () ->
                friendService.sendFriendRequest(requester.getId(), "garde@gmail.com"));

        assertEquals(FriendErrorCode.MEMBER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    public void 본인_계정에_친구_요청_예외() throws Exception {
        //given
        friendService.sendFriendRequest(requester.getId(), receiver.getEmail());

        //when,then
        FriendException exception = assertThrows(FriendException.class, () ->
                friendService.sendFriendRequest(requester.getId(), requester.getEmail()));
        
        assertEquals(FriendErrorCode.SELF_REQUEST, exception.getErrorCode());
    }
    @Test
    public void 중복_친구_요청예외() throws Exception {
        //given
        friendService.sendFriendRequest(requester.getId(), receiver.getEmail());

        //when,then
        FriendException exception = assertThrows(FriendException.class, () ->
                friendService.sendFriendRequest(requester.getId(), receiver.getEmail()));

        assertEquals(FriendErrorCode.ALREADY_REQUESTED, exception.getErrorCode());
    }
    @Test
    public void 이미_친구_상태에서_요청시_예외() throws Exception {
        //given
        Friend friend = Friend.request(requester,receiver);
        friend.accept();
        friendRepository.save(friend);


        //when,then
        FriendException exception = assertThrows(FriendException.class, () ->
                friendService.sendFriendRequest(requester.getId(), receiver.getEmail()));

        assertEquals(FriendErrorCode.ALREADY_FRIEND, exception.getErrorCode());
    }


}
