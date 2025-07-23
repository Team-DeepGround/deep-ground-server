package com.samsamhajo.deepground.address.service;

import com.samsamhajo.deepground.address.dto.AddressDto;
import com.samsamhajo.deepground.address.entity.Address;
import com.samsamhajo.deepground.address.repository.AddressRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class AddressServiceTest {

    @Autowired
    private AddressService addressService;

    @Autowired
    private AddressRepository addressRepository;

    @BeforeEach
    void setUp() {
        addressRepository.save(Address.of("서울특별시", "강남구", "역삼동"));
        addressRepository.save(Address.of("서울특별시", "강남구", "삼성동"));
        addressRepository.save(Address.of("서울특별시", "서초구", "서초동"));
        addressRepository.save(Address.of("부산광역시", "해운대구", "우동"));
    }

    @Test
    void 모든_시_조회() {
        List<AddressDto> cities = addressService.getAllCities();
        assertThat(cities)
                .extracting(AddressDto::getCity)
                .containsExactlyInAnyOrder("서울특별시", "부산광역시");
    }

    @Test
    void 시에_해당하는_구_조회() {
        List<AddressDto> gus = addressService.getGusByCity("서울특별시");
        assertThat(gus)
                .extracting(AddressDto::getGu)
                .containsExactlyInAnyOrder("강남구", "서초구");
    }

    @Test
    void 시와_구에_해당하는_동_조회() {
        List<AddressDto> dongs = addressService.getDongsByCityAndGu("서울특별시", "강남구");
        assertThat(dongs)
                .extracting(AddressDto::getDong)
                .containsExactlyInAnyOrder("삼성동", "역삼동");
    }
}
