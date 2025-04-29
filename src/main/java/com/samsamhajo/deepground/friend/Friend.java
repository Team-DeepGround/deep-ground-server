package com.samsamhajo.deepground.modules.friend;

import com.samsamhajo.deepground.global.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;


@Entity
@Getter
public class Friend extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name ="friend_id")
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "request_member_id" , nullable = false)
    private Member requestMember; //요청 멤버

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "receive_member_id" , nullable = false)
    private Member receiveMember; // 요청을 받은 멤버

    private String Member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FriendStatus status; //REQUEST,CANCEL,ACCEPT,REFUSAL

    // 비즈니스 로직

    protected Friend() {}
    // 친구 요청 생성자
    public Friend(Member requestMember, Member receiveMember) {
        this.requestMember = requestMember;
        this.receiveMember = receiveMember;
        this.status = FriendStatus.REQUEST;
    }

    // 요청 취소
    public void cancel() {
        this.status = FriendStatus.CANCEL;
    }

    // 수락
    public void accept() {
        this.status = FriendStatus.ACCEPT;
    }

    // 거절
    public void refusal() {
        this.status = FriendStatus.REFUSAL;
    }
}
