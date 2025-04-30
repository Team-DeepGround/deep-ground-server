package com.samsamhajo.deepground.friend;

import com.samsamhajo.deepground.global.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;


@Entity
@Getter
@Table( name ="friends")
public class Friend extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    @Column(name ="friend_id")
    private Long id;

    // TODO: Member Entity 구현 시 작성
//    @ManyToOne (fetch = FetchType.LAZY)
//    @JoinColumn(name = "request_member_id" , nullable = false)
//    private Member requestMember;

//    @ManyToOne (fetch = FetchType.LAZY)
//    @JoinColumn(name = "receive_member_id" , nullable = false)
//    private Member receiveMember;


    @Enumerated(EnumType.STRING)
    @Column(name="friend_status", nullable = false)
    private FriendStatus status; //REQUEST,CANCEL,ACCEPT,REFUSAL

    /*비즈니스 로직*/

    protected Friend() {}
    /* 친구 요청 생성자 */
//    public Friend(Member requestMember, Member receiveMember) {
//        this.requestMember = requestMember;
//        this.receiveMember = receiveMember;
//        this.status = FriendStatus.REQUEST;
//    }

    public void cancel() {
        this.status = FriendStatus.CANCEL;
    }

    public void accept() {
        this.status = FriendStatus.ACCEPT;
    }

    public void refusal() {
        this.status = FriendStatus.REFUSAL;
    }
}
