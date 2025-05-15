package com.samsamhajo.deepground.member.repository;

import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.qna.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {


}
