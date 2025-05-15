package com.samsamhajo.deepground.techStack.repository;

import com.samsamhajo.deepground.techStack.entity.MemberTechStack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberTechStackRepository extends JpaRepository<MemberTechStack, Long> {
}
