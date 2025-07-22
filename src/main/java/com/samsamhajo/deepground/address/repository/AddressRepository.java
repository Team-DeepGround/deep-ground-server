package com.samsamhajo.deepground.address.repository;

import com.samsamhajo.deepground.address.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
