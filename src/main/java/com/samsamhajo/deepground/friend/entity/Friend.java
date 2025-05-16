package com.samsamhajo.deepground.friend.entity;

import com.samsamhajo.deepground.global.BaseEntity;
import com.samsamhajo.deepground.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@Table( name ="friends")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Friend extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    @Column(name ="friend_id")
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "request_member_id" , nullable = false)
    private Member requestMember;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "receive_member_id" , nullable = false)
    private Member receiveMember;


    @Enumerated(EnumType.STRING)
    @Column(name="friend_status", nullable = false)
    private FriendStatus status; //REQUEST,CANCEL,ACCEPT,REFUSAL

    private Friend(Member requestMember, Member receiveMember, FriendStatus status) {
        this.requestMember = requestMember;
        this.receiveMember = receiveMember;
        this.status = status;
    }


    public static Friend request (Member requestMember, Member receiveMember) {
      return new Friend (requestMember, receiveMember, FriendStatus.REQUEST);
    }

    public void cancel(Long requesterId) {
        this.status = FriendStatus.CANCEL;
    }

    public void accept() {
        this.status = FriendStatus.ACCEPT;
    }

    public void refusal() {
        this.status = FriendStatus.REFUSAL;
    }
}
