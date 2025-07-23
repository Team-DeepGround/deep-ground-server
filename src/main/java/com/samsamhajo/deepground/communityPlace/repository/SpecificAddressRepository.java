package com.samsamhajo.deepground.communityPlace.repository;

import com.samsamhajo.deepground.communityPlace.entity.SpecificAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecificAddressRepository extends JpaRepository<SpecificAddress, Long> {
}
