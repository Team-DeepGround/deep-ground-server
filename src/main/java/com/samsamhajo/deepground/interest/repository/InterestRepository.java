package com.samsamhajo.deepground.interest.repository;

import com.samsamhajo.deepground.interest.entity.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterestRepository extends JpaRepository<Interest, Long> {
}
