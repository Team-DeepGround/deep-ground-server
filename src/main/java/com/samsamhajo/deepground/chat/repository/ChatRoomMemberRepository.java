package com.samsamhajo.deepground.chat.repository;

import com.samsamhajo.deepground.chat.dto.ChatRoomInfo;
import com.samsamhajo.deepground.chat.entity.ChatRoomMember;

import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {
    List<ChatRoomMember> findByChatRoomId(Long chatRoomId);

    Optional<ChatRoomMember> findByChatRoomIdAndMemberId(Long chatRoomId, Long memberId);

    @Query("SELECT NEW com.samsamhajo.deepground.chat.dto.ChatRoomInfo("
            + "cr.id, friend.nickname, crm.lastReadMessageTime, 2) "
            + "FROM ChatRoomMember crm "
            + "JOIN crm.chatRoom cr "
            + "JOIN ChatRoomMember friendCrm ON friendCrm.chatRoom.id = cr.id AND friendCrm.member.id != crm.member.id "
            + "JOIN friendCrm.member friend "
            + "WHERE crm.member.id = :memberId AND cr.type = 'FRIEND' "
            + "ORDER BY crm.lastReadMessageTime DESC")
    Page<ChatRoomInfo> findByMemberIdAndChatRoomTypeFriend(Long memberId, Pageable pageable);

    @Query("SELECT NEW com.samsamhajo.deepground.chat.dto.ChatRoomInfo("
            + "cr.id, sg.title, crm.lastReadMessageTime, COUNT(members)) "
            + "FROM ChatRoomMember crm "
            + "JOIN crm.chatRoom cr "
            + "JOIN StudyGroup sg ON sg.chatRoom.id = cr.id "
            + "JOIN ChatRoomMember members ON members.chatRoom.id = cr.id "
            + "WHERE crm.member.id = :memberId AND cr.type = 'STUDY_GROUP' "
            + "GROUP BY cr.id, sg.title, crm.lastReadMessageTime "
            + "ORDER BY crm.lastReadMessageTime DESC")
    Page<ChatRoomInfo> findByMemberIdAndChatRoomTypeStudyGroup(Long memberId, Pageable pageable);

    boolean existsByChatRoomIdAndMemberId(Long chatRoomId, Long memberId);

    @Modifying
    @Query("UPDATE ChatRoomMember SET deleted = true WHERE chatRoom.id = :chatRoomId")
    void softDeleteByChatRoomId(@Param("chatRoomId") Long chatRoomId);
}
