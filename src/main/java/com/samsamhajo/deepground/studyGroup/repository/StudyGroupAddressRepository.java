package com.samsamhajo.deepground.studyGroup.repository;

import com.samsamhajo.deepground.studyGroup.entity.StudyGroupAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudyGroupAddressRepository extends JpaRepository<StudyGroupAddress, Long> {

    @Query("""
        SELECT s.studyGroup.id
        FROM StudyGroupAddress s
        WHERE s.address.city = :city
          AND s.address.gu = :gu
          AND s.address.dong = :dong
    """)
    List<Long> findStudyGroupIdByAddress(String city, String gu, String dong);
}
