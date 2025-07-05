package com.samsamhajo.deepground.techStack.repository;

import com.samsamhajo.deepground.techStack.entity.TechStack;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TechStackRepository extends JpaRepository<TechStack, Long> {
    Optional<TechStack> findByName(String name);

    List<TechStack> findAllByNameIn(List<String> names);
}
