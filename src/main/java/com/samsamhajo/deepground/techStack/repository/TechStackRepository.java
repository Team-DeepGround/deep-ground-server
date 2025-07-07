package com.samsamhajo.deepground.techStack.repository;

import com.samsamhajo.deepground.techStack.entity.TechStack;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TechStackRepository extends JpaRepository<TechStack, Long> {
    Optional<TechStack> findByName(String name);

    @Query("SELECT t FROM TechStack t WHERE t.name IN :names")
    List<TechStack> findByNames(@Param("names") List<String> names);
}
