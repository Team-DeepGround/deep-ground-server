package com.samsamhajo.deepground.friend.repository;

import com.samsamhajo.deepground.friend.entity.Friend;

import com.samsamhajo.deepground.friend.entity.FriendStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend,Long> {

}
