package com.samsamhajo.deepground.friend.repository;

import com.samsamhajo.deepground.friend.entity.Friend;

import com.samsamhajo.deepground.friend.entity.FriendStatus;
import com.samsamhajo.deepground.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface FriendRepository extends JpaRepository<Friend,Long> {

    boolean existsByRequestMemberAndReceiveMemberAndStatus(Member requester, Member receiver, FriendStatus status);

    @Query("SELECT f FROM Friend f WHERE f.requestMember.id = :requesterId AND f.status = 'REQUEST'")
    List<Friend> findSentRequests(@Param("requesterId") Long requesterId);

    boolean existsByIdAndReceiveMemberAndStatus(Long id, Member receiveMember, FriendStatus status);

    @Query("SELECT f FROM Friend f WHERE f.receiveMember.id = :receiverId AND f.status = 'REQUEST'")
    List<Friend> findReceiveRequests(@Param("receiverId") Long receiverId);

    @Query("SELECT f FROM Friend f WHERE (f.requestMember.id = :memberId OR f.receiveMember.id = :memberId) AND f.status = :status AND f.deleted = false ")
    List<Friend> findAllByMemberIdAndFriendStatusAndDeletedIs(Long memberId, FriendStatus status);

    @Query("SELECT f FROM Friend f " +
            "WHERE ((f.requestMember.id = :member1Id AND f.receiveMember.id = :member2Id) " +
            "   OR (f.requestMember.id = :member2Id AND f.receiveMember.id = :member1Id)) " +
            "AND f.status = :status")
    Optional<Friend> findFriendship(
            @Param("member1Id") Long member1Id,
            @Param("member2Id") Long member2Id,
            @Param("status") FriendStatus status
    );

    Optional<Friend> findByRequestMemberIdAndReceiveMemberId(Long requestMemberId, Long receiveMemberId);
}
