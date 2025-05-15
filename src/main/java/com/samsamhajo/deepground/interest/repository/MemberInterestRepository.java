package com.samsamhajo.deepground.interest.repository;

import com.samsamhajo.deepground.interest.entity.MemberInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberInterestRepository extends JpaRepository<MemberInterest, Long> {
}
